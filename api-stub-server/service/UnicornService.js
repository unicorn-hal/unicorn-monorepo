'use strict';


/**
 * 緊急要請
 * 緊急要請を行います
 *
 * body Emergency 
 * xUID String 
 * returns Emergency
 **/
exports.unicornEmergencyPOST = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "userLatitude" : 35.681236,
  "userLongitude" : 139.767125,
  "userID" : "1234567890"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 到着通知
 * ロボットの到着通知を行います
 *
 * body Arrival 
 * robotID String 
 * xUID String 
 * returns Arrival
 **/
exports.unicornRobotIDArrivalPOST = function(body,robotID,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "robotLongitude" : 139.767125,
  "robotLatitude" : 35.681236,
  "userID" : "1234567890"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 完了通知
 * ロボットの検査・治療・搬送の完了通知を行います
 *
 * body Complete 
 * robotID String 
 * xUID String 
 * returns Complete
 **/
exports.unicornRobotIDCompletePOST = function(body,robotID,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "robotSupportID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "userID" : "1234567890"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 移動通知
 * ロボットの移動通知を行います
 *
 * body Moving 
 * robotID String 
 * xUID String 
 * returns Moving
 **/
exports.unicornRobotIDMovingPOST = function(body,robotID,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "robotLongitude" : 139.767125,
  "robotLatitude" : 35.681236,
  "userID" : "1234567890"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

