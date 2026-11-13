/// Las fotos de las galerias estan en Cloudinary en tamaño original. Para las
/// tarjetas y el mapa alcanza con una version chica: Cloudinary la genera sola
/// si se le agregan parametros en la URL (ancho, formato y calidad automaticos).
/// Las fotos de la demo (/fotos-demo/, incluidas con el frontend) tienen una
/// copia chica de 480 px en /fotos-demo/chicas/.
/// El resto de las URLs se devuelven tal cual.
export function miniatura(url: string | null | undefined, ancho = 480): string | null {
  if (!url) {
    return null;
  }
  if (url.startsWith('/fotos-demo/') && ancho <= 480) {
    return url.replace('/fotos-demo/', '/fotos-demo/chicas/');
  }
  const marca = '/image/upload/';
  if (!url.includes('res.cloudinary.com') || !url.includes(marca)) {
    return url;
  }
  return url.replace(marca, `${marca}w_${ancho},c_limit,f_auto,q_auto/`);
}
