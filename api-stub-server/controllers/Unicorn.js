'use strict';

var utils = require('../utils/writer.js');
var Unicorn = require('../service/UnicornService');

module.exports.unicornEmergencyPOST = function unicornEmergencyPOST (req, res, next, body, xUID) {
  Unicorn.unicornEmergencyPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.unicornRobotIDArrivalPOST = function unicornRobotIDArrivalPOST (req, res, next, body, robotID, xUID) {
  Unicorn.unicornRobotIDArrivalPOST(body, robotID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.unicornRobotIDCompletePOST = function unicornRobotIDCompletePOST (req, res, next, body, robotID, xUID) {
  Unicorn.unicornRobotIDCompletePOST(body, robotID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.unicornRobotIDMovingPOST = function unicornRobotIDMovingPOST (req, res, next, body, robotID, xUID) {
  Unicorn.unicornRobotIDMovingPOST(body, robotID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
