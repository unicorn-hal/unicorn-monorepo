'use strict';

var utils = require('../utils/writer.js');
var Robot = require('../service/RobotService');

module.exports.robotsGET = function robotsGET (req, res, next, xUID) {
  Robot.robotsGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.robotsPOST = function robotsPOST (req, res, next, body, xUID) {
  Robot.robotsPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.robotsRobotIDDELETE = function robotsRobotIDDELETE (req, res, next, xUID, robotID) {
  Robot.robotsRobotIDDELETE(xUID, robotID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.robotsRobotIDGET = function robotsRobotIDGET (req, res, next, xUID, robotID) {
  Robot.robotsRobotIDGET(xUID, robotID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.robotsRobotIDPUT = function robotsRobotIDPUT (req, res, next, body, robotID, xUID) {
  Robot.robotsRobotIDPUT(body, robotID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.robotsRobotIDPowerPUT = function robotsRobotIDPowerPUT (req, res, next, body, robotID, xUID) {
  Robot.robotsRobotIDPowerPUT(body, robotID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
