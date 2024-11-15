'use strict';


/**
 * アプリ設定取得
 * アプリの設定情報を取得します
 *
 * xUID String 
 * returns inline_response_200
 **/
exports.app_configGET = function(xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "stunServerType" : "AKS",
  "available" : true
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

