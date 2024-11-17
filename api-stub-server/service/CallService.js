'use strict';


/**
 * 通話予約削除
 * 通話予約を削除します
 *
 * xUID String 
 * callReservationID String 
 * no response value expected for this operation
 **/
exports.callsCallReservationIDDELETE = function (xUID, callReservationID) {
  return new Promise(function (resolve, reject) {
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
 * returns CallReservationResponse
 **/
exports.callsCallReservationIDPUT = function (body, callReservationID, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "callReservationID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "callEndTime": "2024-10-12T15:30:00+09:00",
      "doctorID": "1234567890",
      "callStartTime": "2024-10-12T15:00:00+09:00",
      "userID": "1234567890"
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
 * doctorID String 
 * userID String 
 * returns inline_response_200_12
 **/
exports.callsGET = function (xUID, doctorID, userID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "data": [{
        "callReservationID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        "callEndTime": "2024-10-12T15:30:00+09:00",
        "doctorID": "1234567890",
        "callStartTime": "2024-10-12T15:00:00+09:00",
        "userID": "1234567890"
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
 * 通話予約追加
 * 医師とユーザー間の通話予約を追加します
 *
 * body CallReservationRequest 
 * xUID String 
 * returns CallReservationResponse
 **/
exports.callsPOST = function (body, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "callReservationID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "callEndTime": "2024-10-12T15:30:00+09:00",
      "doctorID": "1234567890",
      "callStartTime": "2024-10-12T15:00:00+09:00",
      "userID": "1234567890"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 医師の通話予約一覧取得
 * 医師の通話予約一覧を取得します
 *
 * xUID String 
 * doctorID String 
 * returns inline_response_200_12
 **/
exports.doctorsDoctorIDCallsGET = function (xUID, doctorID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "data": [{
        "callReservationID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        "callEndTime": "2024-10-12T15:30:00+09:00",
        "doctorID": "1234567890",
        "callStartTime": "2024-10-12T15:00:00+09:00",
        "userID": "1234567890"
      }]
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

