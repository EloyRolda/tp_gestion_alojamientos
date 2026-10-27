package com.eloyroldan.tp_final_alojamiento.backend.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/// Estrategia hibrida (ver analisis en la conversacion del proyecto):
/// 1) Proveedores comunes: whitelist en memoria, O(1), sin red.
/// 2) Cualquier otro dominio: se consulta si tiene un registro MX real (o sea,
///    si puede recibir correo), en vez de rechazarlo solo por no estar en una
///    lista fija. Esto evita el problema de fondo de cualquier whitelist:
///    siempre va a rechazar dominios legitimos que no se anticiparon (ej: un
///    dominio institucional de una universidad).
/// El resultado de la consulta DNS se cachea (positivo y negativo) para no
/// pegarle a la red de nuevo con el mismo dominio en cada registro.
@Slf4j
public class DominioEmailValidoValidator implements ConstraintValidator<DominioEmailValido, String> {

    /// Proveedores masivos: se resuelven al instante, sin tocar la red.
    private static final Set<String> DOMINIOS_COMUNES = Set.of(
            "gmail.com",
            "hotmail.com", "hotmail.com.ar",
            "outlook.com", "outlook.com.ar", "outlook.es",
            "live.com", "live.com.ar",
            "yahoo.com", "yahoo.com.ar", "yahoo.es",
            "icloud.com", "me.com",
            "protonmail.com", "proton.me"
    );

    private static final long TTL_CACHE_MS = 30 * 60 * 1000; // 30 minutos
    private static final int TIMEOUT_DNS_MS = 2000;

    /// Cache propia y simple (dominio -> resultado + vencimiento) para no
    /// depender de una libreria externa solo por esto.
    private static final ConcurrentHashMap<String, CacheEntry> CACHE_MX = new ConcurrentHashMap<>();

    private record CacheEntry(boolean tieneMx, long expiraEn) {
        boolean vencido() {
            return System.currentTimeMillis() > expiraEn;
        }
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || !email.contains("@")) {
            // Se deja pasar: @NotBlank / @Email ya se encargan de ese caso,
            // no queremos duplicar el mismo error con un mensaje distinto.
            return true;
        }
        String dominio = email.substring(email.indexOf('@') + 1).trim().toLowerCase();

        if (DOMINIOS_COMUNES.contains(dominio)) {
            return true;
        }
        return tieneRegistroMx(dominio);
    }

    private boolean tieneRegistroMx(String dominio) {
        CacheEntry cacheado = CACHE_MX.get(dominio);
        if (cacheado != null && !cacheado.vencido()) {
            return cacheado.tieneMx();
        }

        boolean resultado = consultarMxPorDns(dominio);
        CACHE_MX.put(dominio, new CacheEntry(resultado, System.currentTimeMillis() + TTL_CACHE_MS));
        return resultado;
    }

    /// Usa la API estandar del JDK (javax.naming), sin dependencias nuevas.
    /// Fail-open: si el DNS no responde o hay un problema de red transitorio,
    /// se deja pasar el registro en vez de bloquear a un usuario legitimo por
    /// un problema que no es suyo — igual el flujo de verificacion de correo
    /// (obligatorio para poder loguearse) termina filtrando cualquier dominio
    /// que en verdad no pueda recibir el mail de confirmacion.
    private boolean consultarMxPorDns(String dominio) {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            env.put("com.sun.jndi.dns.timeout.initial", String.valueOf(TIMEOUT_DNS_MS));
            env.put("com.sun.jndi.dns.timeout.retries", "1");

            InitialDirContext contexto = new InitialDirContext(env);
            Attributes atributos = contexto.getAttributes(dominio, new String[]{"MX"});
            Attribute mx = atributos.get("MX");
            boolean tieneMx = mx != null && mx.size() > 0;

            if (!tieneMx) {
                log.info("Dominio '{}' sin registro MX: se rechaza como email invalido.", dominio);
            }
            return tieneMx;
        } catch (NamingException e) {
            log.warn("No se pudo resolver MX para '{}' (se deja pasar por las dudas): {}", dominio, e.getMessage());
            return true;
        }
    }
}
