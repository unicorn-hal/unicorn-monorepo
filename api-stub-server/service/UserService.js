'use strict';


/**
 * ユーザー削除
 * ユーザーを削除します
 *
 * xUID String 
 * no response value expected for this operation
 **/
exports.userDELETE = function(xUID) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * ユーザー情報取得
 * ユーザー情報を取得します
 *
 * xUID String 
 * returns User
 **/
exports.userGET = function(xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "uid" : "1234567890",
  "fcmTokenId" : "fcm_token_id"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * ユーザー登録
 * ユーザーを登録します
 *
 * body UserRequest 
 * xUID String 
 * returns UserRequest
 **/
exports.userPOST = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "uid" : "1234567890",
  "fcmTokenId" : "fcm_token_id"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * ユーザープロフィール削除
 * ユーザーの個人情報を削除する
 *
 * xUID String 
 * userUid String 
 * no response value expected for this operation
 **/
exports.usersUserUidProfilesDELETE = function(xUID,userUid) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * ユーザープロフィール取得
 * ユーザーのプロフィールデータを取得します
 *
 * xUID String 
 * userUid String 
 * returns UserProfiles
 **/
exports.usersUserUidProfilesGET = function(xUID,userUid) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "uid" : "1234567890",
  "address" : "東京都新宿区1-1-1",
  "phoneNumber" : "090-1234-5678",
  "occupation" : "エンジニア",
  "gender" : "male",
  "iconImage" : "",
  "postalCode" : "123-4567",
  "bodyHeight" : 180,
  "userName" : "山田 太郎",
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
 * ユーザープロフィール登録
 * ユーザーの個人情報を登録する
 *
 * body UserProfilesRequest 
 * userUid String 
 * xUID String 
 * returns UserProfilesRequest
 **/
exports.usersUserUidProfilesPOST = function(body,userUid,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "address" : "東京都新宿区1-1-1",
  "phoneNumber" : "090-1234-5678",
  "occupation" : "エンジニア",
  "gender" : "male",
  "iconImage" : "",
  "postalCode" : "123-4567",
  "bodyHeight" : 180,
  "userName" : "山田 太郎",
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
 * body UserProfilesRequest 
 * userUid String 
 * xUID String 
 * returns UserProfilesRequest
 **/
exports.usersUserUidProfilesPUT = function(body,userUid,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "address" : "東京都新宿区1-1-1",
  "phoneNumber" : "090-1234-5678",
  "occupation" : "エンジニア",
  "gender" : "male",
  "iconImage" : "",
  "postalCode" : "123-4567",
  "bodyHeight" : 180,
  "userName" : "山田 太郎",
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

