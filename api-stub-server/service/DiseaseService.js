'use strict';


/**
 * 有名な病気一覧取得
 * 有名な病気一覧を取得します
 *
 * xUID String 
 * returns inline_response_200_9
 **/
exports.diseasesFamousGET = function(xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "diseaseName" : "高血圧",
    "diseaseID" : 1
  }, {
    "diseaseName" : "高血圧",
    "diseaseID" : 1
  } ]
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 病気検索
 * 病気名で病気を検索します(部分一致検索&ひらがな漢字検索)。QueryParamsに何も指定しない場合は、空配列が返却される
 *
 * xUID String 
 * diseaseName String 病気名の部分一致検索 (optional)
 * returns inline_response_200_9
 **/
exports.diseasesGET = function(xUID,diseaseName) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "diseaseName" : "高血圧",
    "diseaseID" : 1
  }, {
    "diseaseName" : "高血圧",
    "diseaseID" : 1
  } ]
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

