'use strict';


/**
 * 病院一覧取得
 * 病院一覧を取得します
 *
 * xUID String 
 * returns inline_response_200_2
 **/
exports.hospitalsGET = function(xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "address" : "東京都新宿区1-1-1",
    "phoneNumber" : "090-1234-5678",
    "hospitalID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "postalCode" : "123-4567",
    "hospitalName" : "東京都立大学病院"
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
 * 病院情報取得
 * 病院情報を取得します
 *
 * xUID String 
 * hospitalID String 
 * returns Hospital
 **/
exports.hospitalsHospitalIDGET = function(xUID,hospitalID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "address" : "東京都新宿区1-1-1",
  "phoneNumber" : "090-1234-5678",
  "hospitalID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "postalCode" : "123-4567",
  "hospitalName" : "東京都立大学病院"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

