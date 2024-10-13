'use strict';

var utils = require('../utils/writer.js');
var Call = require('../service/CallService');

module.exports.callsCallReservationIDDELETE = function callsCallReservationIDDELETE (req, res, next, xUID, callReservationID) {
  Call.callsCallReservationIDDELETE(xUID, callReservationID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.callsCallReservationIDPUT = function callsCallReservationIDPUT (req, res, next, body, callReservationID, xUID) {
  Call.callsCallReservationIDPUT(body, callReservationID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.callsGET = function callsGET (req, res, next, xUID, doctorID, userID) {
  Call.callsGET(xUID, doctorID, userID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.callsPOST = function callsPOST (req, res, next, body, xUID) {
  Call.callsPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
