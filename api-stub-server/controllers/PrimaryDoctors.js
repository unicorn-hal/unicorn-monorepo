'use strict';

var utils = require('../utils/writer.js');
var PrimaryDoctors = require('../service/PrimaryDoctorsService');

module.exports.primary_doctorsDoctorIDDELETE = function primary_doctorsDoctorIDDELETE (req, res, next, xUID, doctorID) {
  PrimaryDoctors.primary_doctorsDoctorIDDELETE(xUID, doctorID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.primary_doctorsDoctorIDUsersGET = function primary_doctorsDoctorIDUsersGET (req, res, next, xUID, doctorID) {
  PrimaryDoctors.primary_doctorsDoctorIDUsersGET(xUID, doctorID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.primary_doctorsGET = function primary_doctorsGET (req, res, next, xUID) {
  PrimaryDoctors.primary_doctorsGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.primary_doctorsPOST = function primary_doctorsPOST (req, res, next, body, xUID) {
  PrimaryDoctors.primary_doctorsPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
