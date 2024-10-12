'use strict';


/**
 * 家族メールアドレス削除
 * 家族メールアドレスを削除します
 *
 * xUID String 
 * familyEmailID String 
 * no response value expected for this operation
 **/
exports.family_emailsFamilyEmailIDDELETE = function(xUID,familyEmailID) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * 家族メールアドレス更新
 * 家族メールアドレスを更新します
 *
 * body FamilyEmailRequest 
 * familyEmailID String 
 * xUID String 
 * returns FamilyEmail
 **/
exports.family_emailsFamilyEmailIDPUT = function(body,familyEmailID,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "familyEmailID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "familyName" : "山田 太郎",
  "email" : "sample@sample.com"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 家族メールアドレス一覧取得
 * 家族メールアドレス一覧を取得します
 *
 * xUID String 
 * returns inline_response_200_7
 **/
exports.family_emailsGET = function(xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "familyEmailID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "familyName" : "山田 太郎",
    "email" : "sample@sample.com"
  }, {
    "familyEmailID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "familyName" : "山田 太郎",
    "email" : "sample@sample.com"
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
 * 家族メールアドレス登録
 * 家族メールアドレスを登録します
 *
 * body FamilyEmailRequest 
 * xUID String 
 * returns FamilyEmail
 **/
exports.family_emailsPOST = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "familyEmailID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "familyName" : "山田 太郎",
  "email" : "sample@sample.com"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}
