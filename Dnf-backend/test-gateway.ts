import axios from 'axios';

async function test() {
  try {
    const res = await axios.get('http://localhost:8080/api/pets');
    console.log('GET /api/pets ->', res.status, res.data);
  } catch (err: any) {
    console.error('GET /api/pets -> ERROR', err.response?.status, err.response?.data);
  }

  try {
    const res = await axios.get('http://localhost:8080/api/usuarios');
    console.log('GET /api/usuarios ->', res.status, res.data);
  } catch (err: any) {
    console.error('GET /api/usuarios -> ERROR', err.response?.status, err.response?.data);
  }

  try {
    const res = await axios.get('http://localhost:8080/api/success-stories');
    console.log('GET /api/success-stories ->', res.status, res.data);
  } catch (err: any) {
    console.error('GET /api/success-stories -> ERROR', err.response?.status, err.response?.data);
  }
}

test();