'use strict';


/**
 * 主治医取得
 * ユーザーの主治医情報を取得します
 *
 * xUID String 
 * returns inline_response_200
 **/
exports.primary_doctorsGET = function(xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "firstName" : "太郎",
    "lastName" : "山田",
    "chatSupportHours" : "9:00-17:00",
    "phoneNumber" : "090-1234-5678",
    "callSupportHours" : "9:00-17:00",
    "doctorID" : "1234567890",
    "departments" : [ {
      "departmentName" : "内科",
      "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    }],
    "hospital" : {
      "hospitalID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "hospitalName" : "東京都立大学病院"
    },
    "doctorIcon" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
    "email" : "sample@mail.com"
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
 * 主治医登録
 * ユーザーの主治医を登録します
 *
 * body PrimaryDoctorsRequest 
 * xUID String 
 * returns PrimaryDoctorsResponse
 **/
exports.primary_doctorsPOST = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "doctorIDs" : [ "1234567890", "1234567890" ],
  "userID" : "1234567890"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 主治医更新
 * ユーザーの主治医を更新します
 *
 * body PrimaryDoctorsRequest 
 * xUID String 
 * returns PrimaryDoctorsResponse
 **/
exports.primary_doctorsPUT = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "doctorIDs" : [ "1234567890", "1234567890" ],
  "userID" : "1234567890"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

