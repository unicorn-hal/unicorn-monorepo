'use strict';


/**
 * ユーザープロフィール登録
 * ユーザーの個人情報を登録する
 *
 * body UserRequest 
 * xUID String 
 * returns UserResponse
 **/
exports.usersPOST = function (body, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
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
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * ユーザー削除
 * ユーザーを削除する
 *
 * xUID String 
 * userID String 
 * no response value expected for this operation
 **/
exports.usersUserIDDELETE = function (xUID, userID) {
  return new Promise(function (resolve, reject) {
    resolve();
  });
}


/**
 * ユーザープロフィール取得
 * ユーザーのプロフィールデータを取得します
 *
 * xUID String 
 * userID String 
 * returns User
 **/
exports.usersUserIDGET = function (xUID, userID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
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
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 通知設定取得
 * ユーザーの通知設定を取得します
 *
 * xUID String 
 * userID String 
 * returns UserNotification
 **/
exports.usersUserIDNotificationGET = function (xUID, userID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "isMedicineReminder": true,
      "isHospitalNews": true,
      "isRegularHealthCheckup": true
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 通知設定更新
 * ユーザーの通知設定を更新します
 *
 * body UserNotification 
 * userID String 
 * xUID String 
 * returns UserNotification
 **/
exports.usersUserIDNotificationPOST = function (body, userID, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "isMedicineReminder": true,
      "isHospitalNews": true,
      "isRegularHealthCheckup": true
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 通知設定更新
 * ユーザーの通知設定を更新します
 *
 * body UserNotification 
 * userID String 
 * xUID String 
 * returns UserNotification
 **/
exports.usersUserIDNotificationPUT = function (body, userID, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "isMedicineReminder": true,
      "isHospitalNews": true,
      "isRegularHealthCheckup": true
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * ユーザープロフィール更新
 * ユーザーの個人情報を更新する
 *
 * body UserRequest 
 * userID String 
 * xUID String 
 * returns UserResponse
 **/
exports.usersUserIDPUT = function (body, userID, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
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
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

