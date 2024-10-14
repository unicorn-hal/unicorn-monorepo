'use strict';


/**
 * メッセージ一覧表示
 * メッセージを一覧表示します。
 *
 * xUID String 
 * chatID String 
 * returns inline_response_200_5
 **/
exports.chatsChatIDMessagesGET = function(xUID,chatID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "firstName" : "太郎",
    "lastName" : "山田",
    "senderID" : "1234567890",
    "chatID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "iconImage" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
    "messageID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "sentAt" : "2020-01-01T00:00:00Z",
    "content" : "Hello, World!"
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
 * メッセージ削除
 * メッセージを削除します
 *
 * xUID String 
 * chatID String 
 * messageID String 
 * no response value expected for this operation
 **/
exports.chatsChatIDMessagesMessageIDDELETE = function(xUID,chatID,messageID) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * メッセージ送信
 * メッセージを送信します
 *
 * body MessageRequest 
 * chatID String 
 * xUID String 
 * returns MessageResponse
 **/
exports.chatsChatIDMessagesPOST = function(body,chatID,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "senderID" : "1234567890",
  "messageID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "content" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * チャット一覧取得
 * チャット一覧を取得します(userプロパティは、医師の管理者画面で使用します)
 *
 * xUID String 
 * returns inline_response_200_4
 **/
exports.chatsGET = function(xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "doctor" : {
      "firstName" : "太郎",
      "lastName" : "山田",
      "doctorID" : "1234567890",
      "doctorIcon" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE="
    },
    "latestMessageSentAt" : "2020-01-01T00:00:00Z",
    "chatID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "latestMessageText" : "Hello, World!",
    "user" : {
      "firstName" : "太郎",
      "lastName" : "山田",
      "userIcon" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
      "userID" : "1234567890"
    }
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
 * チャット作成
 * 新しいチャットを作成します
 *
 * body ChatRequest 
 * xUID String 
 * returns ChatResponse
 **/
exports.chatsPOST = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "chatID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "doctorID" : "1234567890",
  "userID" : "1234567890"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

