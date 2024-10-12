'use strict';

var utils = require('../utils/writer.js');
var Chat = require('../service/ChatService');

module.exports.chatsChatIDMessagesGET = function chatsChatIDMessagesGET (req, res, next, xUID, chatID) {
  Chat.chatsChatIDMessagesGET(xUID, chatID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.chatsChatIDMessagesMessageIDDELETE = function chatsChatIDMessagesMessageIDDELETE (req, res, next, xUID, chatID, messageID) {
  Chat.chatsChatIDMessagesMessageIDDELETE(xUID, chatID, messageID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.chatsChatIDMessagesPOST = function chatsChatIDMessagesPOST (req, res, next, body, chatID, xUID) {
  Chat.chatsChatIDMessagesPOST(body, chatID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.chatsGET = function chatsGET (req, res, next, xUID) {
  Chat.chatsGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.chatsPOST = function chatsPOST (req, res, next, body, xUID) {
  Chat.chatsPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
