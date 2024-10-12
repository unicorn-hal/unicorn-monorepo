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
