import { create } from 'axios';

var axiosInstance = create({
    // baseURL: 'http://localhost:5000/',
    // baseURL: 'http://192.168.10.119:5000/'
    baseURL: '/'

});

export default axiosInstance;
