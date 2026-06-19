export const fetchWithAuth = async (url: string, options: RequestInit = {}) => {
  const token = localStorage.getItem('token');
  
  const headers = new Headers(options.headers || {});
  
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }
  
  console.log(`[fetchWithAuth] Enviando petición a: ${url} (Método: ${options.method || 'GET'})`);
  
  try {
    const response = await fetch(url, {
      ...options,
      headers
    });
    
    console.log(`[fetchWithAuth] Respuesta de ${url}: ${response.status} ${response.statusText}`);
    
    if (response.status === 401) {
      console.warn('[fetchWithAuth] Token inválido o expirado. Redirigiendo a login...');
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
      throw new Error('No autorizado. Sesión expirada.');
    }
    
    return response;
  } catch (error) {
    console.error(`[fetchWithAuth] Error de red o CORS al intentar acceder a ${url}:`, error);
    throw error;
  }
};
