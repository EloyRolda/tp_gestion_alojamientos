// Helper minimo para llamar a la API con el token JWT guardado en localStorage.
// No es "diseno": es la unica logica repetida entre TODAS las paginas (leer el
// token, mandarlo en el header, y mandar a login si venció), asi cada HTML
// individual queda con el minimo JS posible para lo suyo.

const API_BASE = "";

function getToken() {
    return localStorage.getItem("token");
}

function requireLogin() {
    if (!getToken()) {
        window.location.href = "/login.html";
    }
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "/login.html";
}

/// fetch con el header Authorization ya puesto. Si el server devuelve 401,
/// manda directo a login (token vencido o invalido).
async function apiFetch(path, options = {}) {
    const headers = options.headers ? { ...options.headers } : {};
    const token = getToken();
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const response = await fetch(API_BASE + path, { ...options, headers });

    if (response.status === 401) {
        logout();
        throw new Error("Sesion vencida");
    }
    if (response.status === 403) {
        window.location.href = "/403.html";
        throw new Error("No autorizado");
    }
    return response;
}

/// Intenta parsear como JSON; si no es JSON valido (el backend devuelve varios
/// errores como texto plano, no como JSON), devuelve el texto tal cual.
function parsearJsonSeguro(texto) {
    try {
        return JSON.parse(texto);
    } catch (e) {
        return texto;
    }
}

/// Idem apiFetch pero ya asume JSON de ida y vuelta, y tira error con el texto del backend si falla.
async function apiJson(path, method = "GET", body = null) {
    const options = { method, headers: { "Content-Type": "application/json" } };
    if (body !== null) {
        options.body = JSON.stringify(body);
    }
    const response = await apiFetch(path, options);
    const texto = await response.text();

    if (!response.ok) {
        const datos = texto ? parsearJsonSeguro(texto) : null;
        const mensaje = typeof datos === "string" ? datos : (datos?.message || "Error en la solicitud");
        throw new Error(mensaje);
    }

    return texto ? parsearJsonSeguro(texto) : null;
}

/// Decodifica el payload del JWT (sin validar firma, solo para leer el rol en el front).
function payloadToken() {
    const token = getToken();
    if (!token) return null;
    try {
        return JSON.parse(atob(token.split(".")[1]));
    } catch (e) {
        return null;
    }
}
