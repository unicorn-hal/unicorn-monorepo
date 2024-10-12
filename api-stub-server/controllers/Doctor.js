'use strict';

var utils = require('../utils/writer.js');
var Doctor = require('../service/DoctorService');

module.exports.doctorsDoctorUidDELETE = function doctorsDoctorUidDELETE (req, res, next, xUID, doctorUid) {
  Doctor.doctorsDoctorUidDELETE(xUID, doctorUid)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.doctorsDoctorUidGET = function doctorsDoctorUidGET (req, res, next, xUID, doctorUid) {
  Doctor.doctorsDoctorUidGET(xUID, doctorUid)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.doctorsDoctorUidPUT = function doctorsDoctorUidPUT (req, res, next, body, doctorUid, xUID) {
  Doctor.doctorsDoctorUidPUT(body, doctorUid, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.doctorsGET = function doctorsGET (req, res, next, doctorName, departmentId, hospitalID) {
  Doctor.doctorsGET(doctorName, departmentId, hospitalID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.doctorsPOST = function doctorsPOST (req, res, next, body, xUID) {
  Doctor.doctorsPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.hospitalsHospitalIDDoctorsGET = function hospitalsHospitalIDDoctorsGET (req, res, next, xUID, hospitalID) {
  Doctor.hospitalsHospitalIDDoctorsGET(xUID, hospitalID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
