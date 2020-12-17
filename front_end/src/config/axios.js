import { create } from 'axios';

var axiosInstance = create({
    // baseURL: 'http://localhost:5000/',
    baseURL: 'http://192.168.250.17:8000/'
    // baseURL: '/'

});

export default axiosInstance;
