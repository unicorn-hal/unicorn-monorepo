'use strict';


/**
 * 検査結果削除
 * ユーザーの健康診断結果を削除します
 *
 * xUID String 
 * healthCheckupID String 
 * no response value expected for this operation
 **/
exports.health_checkupsHealthCheckupIDDELETE = function (xUID, healthCheckupID) {
  return new Promise(function (resolve, reject) {
    resolve();
  });
}


/**
 * 検査結果登録
 * ユーザーの健康診断結果を登録します
 *
 * body HealthCheckupRequest 
 * xUID String 
 * returns HealthCheckupResponse
 **/
exports.health_checkupsPOST = function (body, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "date": "2020-01-01T00:00:00.000+00:00",
      "bloodPressure": "120/80",
      "healthCheckupID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "bodyTemperature": 36.5,
      "medicalRecord": "## 2020年1月1日\n- 体温: 36.5\n- 血圧: 120/80\n- 診断: 軽度の風邪"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 検査結果一覧取得
 * 一週間分の検査結果を取得します
 *
 * xUID String 
 * userID String 
 * returns inline_response_200_2
 **/
exports.usersUserIDHealth_checkupsGET = function (xUID, userID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "data": [{
        "date": "2020-01-01T00:00:00.000+00:00",
        "bloodPressure": "120/80",
        "healthCheckupID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
        "bodyTemperature": 36.5,
        "medicalRecord": "## 2020年1月1日\n- 体温: 36.5\n- 血圧: 120/80\n- 診断: 軽度の風邪"
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
 * 検査結果取得
 * ユーザーの健康診断結果を取得します
 *
 * xUID String 
 * healthCheckupID String 
 * userID String 
 * returns HealthCheckup
 **/
exports.usersUserIDHealth_checkupsHealthCheckupIDGET = function (xUID, healthCheckupID, userID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "date": "2020-01-01T00:00:00.000+00:00",
      "bloodPressure": "120/80",
      "healthCheckupID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "bodyTemperature": 36.5,
      "medicalRecord": "## 2020年1月1日\n- 体温: 36.5\n- 血圧: 120/80\n- 診断: 軽度の風邪"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 検査結果更新
 * ユーザーの健康診断結果を更新します
 *
 * body HealthCheckupRequest 
 * healthCheckupID String 
 * userID String 
 * xUID String 
 * returns HealthCheckupResponse
 **/
exports.usersUserIDHealth_checkupsHealthCheckupIDPUT = function (body, healthCheckupID, userID, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "date": "2020-01-01T00:00:00.000+00:00",
      "bloodPressure": "120/80",
      "healthCheckupID": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "bodyTemperature": 36.5,
      "medicalRecord": "## 2020年1月1日\n- 体温: 36.5\n- 血圧: 120/80\n- 診断: 軽度の風邪"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

