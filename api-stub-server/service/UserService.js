'use strict';


/**
 * ユーザープロフィール登録
 * ユーザーの個人情報を登録する
 *
 * body UserRequest 
 * xUID String 
 * returns UserResponse
 **/
exports.usersPOST = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "firstName" : "太郎",
  "lastName" : "山田",
  "phoneNumber" : "090-1234-5678",
  "occupation" : "エンジニア",
  "iconImage" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
  "bodyHeight" : 180,
  "userID" : "1234567890",
  "bodyWeight" : 75
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
exports.usersUserIDDELETE = function(xUID,userID) {
  return new Promise(function(resolve, reject) {
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
exports.usersUserIDGET = function(xUID,userID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "firstName" : "太郎",
  "lastName" : "山田",
  "address" : "東京都新宿区1-1-1",
  "phoneNumber" : "090-1234-5678",
  "occupation" : "エンジニア",
  "gender" : "male",
  "iconImage" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
  "postalCode" : "123-4567",
  "bodyHeight" : 180,
  "userID" : "1234567890",
  "birthDate" : "1990-01-01T00:00:00.000+00:00",
  "bodyWeight" : 75
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
exports.usersUserIDPUT = function(body,userID,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "firstName" : "太郎",
  "lastName" : "山田",
  "phoneNumber" : "090-1234-5678",
  "occupation" : "エンジニア",
  "iconImage" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
  "bodyHeight" : 180,
  "userID" : "1234567890",
  "bodyWeight" : 75
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

