'use strict';


/**
 * 通話予約削除
 * 通話予約を削除します
 *
 * xUID String 
 * callReservationID String 
 * no response value expected for this operation
 **/
exports.callsCallReservationIDDELETE = function(xUID,callReservationID) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * 通話予約更新
 * 通話予約の日時を更新します
 *
 * body CallReservationRequest 
 * callReservationID String 
 * xUID String 
 * returns CallReservation
 **/
exports.callsCallReservationIDPUT = function(body,callReservationID,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "callReservationID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "doctorUid" : "1234567890",
  "userID" : "1234567890",
  "callTime" : "2024-10-12T15:00:00Z"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 通話予約取得
 * 医師とユーザーの通話予約情報を取得します
 *
 * xUID String 
 * doctorUid String 
 * userID String 
 * returns CallReservation
 **/
exports.callsGET = function(xUID,doctorUid,userID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "callReservationID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "doctorUid" : "1234567890",
  "userID" : "1234567890",
  "callTime" : "2024-10-12T15:00:00Z"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 通話予約追加
 * 医師とユーザー間の通話予約を追加します
 *
 * body CallReservationRequest 
 * xUID String 
 * returns CallReservation
 **/
exports.callsPOST = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "callReservationID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "doctorUid" : "1234567890",
  "userID" : "1234567890",
  "callTime" : "2024-10-12T15:00:00Z"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

