'use strict';


/**
 * アカウント削除
 * アカウントを削除します
 *
 * xUID String 
 * no response value expected for this operation
 **/
exports.accountsDELETE = function (xUID) {
  return new Promise(function (resolve, reject) {
    resolve();
  });
}


/**
 * アカウント情報取得
 * アカウント情報を取得します
 *
 * xUID String 
 * returns Account
 **/
exports.accountsGET = function (xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "uid": "1234567890",
      "role": "user",
      "fcmTokenId": "fcm_token_id"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * アカウント登録
 * アカウントを登録します
 *
 * body AccountRequest 
 * xUID String 
 * returns AccountResponse
 **/
exports.accountsPOST = function (body, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "uid": "1234567890",
      "role": "user",
      "fcmTokenId": "fcm_token_id"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * アカウント更新
 * アカウント情報を更新します
 *
 * body Accounts_body 
 * xUID String 
 * returns accounts_body
 **/
exports.accountsPUT = function (body, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "fcmTokenId": "fcm_token_id"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * アカウント情報取得
 * アカウント情報を取得します。医者のみが取得できます
 *
 * xUID String 
 * uid String 
 * returns Account
 **/
exports.accountsUidGET = function (xUID, uid) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "uid": "1234567890",
      "role": "user",
      "fcmTokenId": "fcm_token_id"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

