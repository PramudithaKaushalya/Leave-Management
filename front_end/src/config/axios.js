import { create } from 'axios';

var axiosInstance = create({
<<<<<<< HEAD
    // baseURL: 'http://localhost:5000/',
    // baseURL: 'http://192.168.10.119:5000/'
    baseURL: '/'
=======
    baseURL: 'http://localhost:5000/',
    // baseURL: 'http://192.168.10.119:5000/'
    // baseURL: '/'
>>>>>>> 713a79ac36389ba218887cdbcaecfed17513f028

});

export default axiosInstance;
