axios.defaults.baseURL = "http://localhost:10030/services";
Vue.prototype.$http = axios;



/**
 * url字符串转对象
 * @param url
 * @returns {{}}
 */
function parseUrlParams2Obj(url){  //
    let paramStr = url.split("?")[1];
    let paramObj = {};
    if(paramStr){
        let paramArray = paramStr.split("&");
        $.each(paramArray,function (index,param){
            let paramName = param.split("=")[0];
            let paramValue = param.split("=")[1];
            paramObj[paramName] = paramValue;
        })
    }
    return paramObj;
}