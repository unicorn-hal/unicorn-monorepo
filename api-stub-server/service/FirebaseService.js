'use strict';


/**
 * Firebaseアカウント作成
 * Firebaseアカウントを作成します
 *
 * body FirebaseAccountRequest 
 * xUID String 
 * returns FirebaseAccountResponse
 **/
exports.firebaseAccountsPOST = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "uid" : "1234567890",
  "password" : "password",
  "email" : "test@test.com"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

