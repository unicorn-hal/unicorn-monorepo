'use strict';


/**
 * 主治医削除
 * ユーザーの主治医を削除します
 *
 * xUID String 
 * doctorID String 
 * no response value expected for this operation
 **/
exports.primary_doctorsDoctorIDDELETE = function (xUID, doctorID) {
  return new Promise(function (resolve, reject) {
    resolve();
  });
}


/**
 * 主治医患者取得
 * 主治医の患者情報を取得します
 *
 * xUID String 
 * doctorID String 
 * returns inline_response_200_2
 **/
exports.primary_doctorsDoctorIDUsersGET = function (xUID, doctorID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "data": [{
        "lastName": "山田",
        "address": "東京都新宿区1-1-1",
        "occupation": "エンジニア",
        "gender": "male",
        "iconImageUrl": "https://placehold.jp/150x150.png",
        "postalCode": "1234567",
        "bodyHeight": 180.5,
        "userID": "1234567890",
        "birthDate": "1990-01-01T00:00:00.000+00:00",
        "firstName": "太郎",
        "phoneNumber": "09012345678",
        "bodyWeight": 75.5,
        "email": "test@test.com"
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
 * 主治医取得
 * ユーザーの主治医情報を取得します
 *
 * xUID String 
 * returns inline_response_200_1
 **/
exports.primary_doctorsGET = function (xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "data": [{
        "firstName": "太郎",
        "lastName": "山田",
        "doctorIconUrl": "https://placehold.jp/150x150.png",
        "chatSupportHours": "9:00-17:00",
        "phoneNumber": "09012345678",
        "callSupportHours": "9:00-17:00",
        "doctorID": "1234567890",
        "departments": [{
          "departmentName": "内科",
          "departmentID": "f47ac10b-58cc-4372-a567-0e02b2c3d479"
        }],
        "hospital": {
          "hospitalID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
          "hospitalName": "東京都立大学病院"
        },
        "email": "sample@mail.com"
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
exports.primary_doctorsPOST = function (body, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "doctorID": "1234567890"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

