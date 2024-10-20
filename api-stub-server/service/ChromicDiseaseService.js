'use strict';


/**
 * 持病削除
 * 持病を削除します
 *
 * xUID String 
 * diseaseName String 
 * no response value expected for this operation
 **/
exports.chronic_diseasesDELETE = function(xUID,diseaseName) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * 持病一覧取得
 * 登録してある持病名を取得します
 *
 * xUID String 
 * returns inline_response_200_9
 **/
exports.chronic_diseasesGET = function(xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "diseaseName" : "高血圧",
    "diseaseID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
  }]
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 持病追加
 * 検索した持病を追加します
 *
 * body ChronicDiseaseRequest 
 * returns ChronicDiseaseResponse
 **/
exports.chronic_diseasesPOST = function(body) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "diseaseName" : "高血圧",
  "diseaseID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

