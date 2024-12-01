'use strict';

var utils = require('../utils/writer.js');
var Robot = require('../service/RobotService');

module.exports.robotGET = function robotGET (req, res, next, xUID) {
  Robot.robotGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.robotPOST = function robotPOST (req, res, next, body, xUID) {
  Robot.robotPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.robotRobotIDDELETE = function robotRobotIDDELETE (req, res, next, xUID, robotID) {
  Robot.robotRobotIDDELETE(xUID, robotID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.robotRobotIDGET = function robotRobotIDGET (req, res, next, xUID, robotID) {
  Robot.robotRobotIDGET(xUID, robotID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.robotRobotIDPUT = function robotRobotIDPUT (req, res, next, body, robotID, xUID) {
  Robot.robotRobotIDPUT(body, robotID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
