'use strict';


/**
 * ロボット一覧取得
 * ロボット一覧を取得します
 *
 * xUID String 
 * returns inline_response_200_15
 **/
exports.robotsGET = function (xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "data": [{
        "robotName": "Unicorn",
        "robotID": "1234567890"
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
 * ロボット追加
 * 新しいロボットを追加します
 *
 * body RobotRequest 
 * xUID String 
 * returns RobotResponse
 **/
exports.robotsPOST = function (body, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "robotName": "Unicorn"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * ロボット削除
 * ロボットを削除します
 *
 * xUID String 
 * robotID String 
 * no response value expected for this operation
 **/
exports.robotsRobotIDDELETE = function (xUID, robotID) {
  return new Promise(function (resolve, reject) {
    resolve();
  });
}


/**
 * ロボット情報取得
 * ロボットの情報を取得します
 *
 * xUID String 
 * robotID String 
 * returns Robot
 **/
exports.robotsRobotIDGET = function (xUID, robotID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "robotName": "Unicorn",
      "robotID": "1234567890"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * ロボット情報更新
 * ロボットの情報を更新します
 *
 * body RobotRequest 
 * robotID String 
 * xUID String 
 * returns RobotResponse
 **/
exports.robotsRobotIDPUT = function (body, robotID, xUID) {
  return new Promise(function (resolve, reject) {
    var examples = {};
    examples['application/json'] = {
      "robotName": "Unicorn"
    };
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

