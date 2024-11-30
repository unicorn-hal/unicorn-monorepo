'use strict';

var utils = require('../utils/writer.js');
var Hospital = require('../service/HospitalService');

module.exports.hospitalsGET = function hospitalsGET (req, res, next, xUID) {
  Hospital.hospitalsGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.hospitalsHospitalIDGET = function hospitalsHospitalIDGET (req, res, next, xUID, hospitalID) {
  Hospital.hospitalsHospitalIDGET(xUID, hospitalID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.hospitalsHospitalIDNewsGET = function hospitalsHospitalIDNewsGET (req, res, next, xUID, hospitalID) {
  Hospital.hospitalsHospitalIDNewsGET(xUID, hospitalID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.hospitalsHospitalIDNewsNewsIDDELETE = function hospitalsHospitalIDNewsNewsIDDELETE (req, res, next, xUID, hospitalID, newsID) {
  Hospital.hospitalsHospitalIDNewsNewsIDDELETE(xUID, hospitalID, newsID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.hospitalsHospitalIDNewsPOST = function hospitalsHospitalIDNewsPOST (req, res, next, body, hospitalID, xUID) {
  Hospital.hospitalsHospitalIDNewsPOST(body, hospitalID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
