'use strict';

var utils = require('../utils/writer.js');
var Doctor = require('../service/DoctorService');

module.exports.doctorsDoctorIDDELETE = function doctorsDoctorIDDELETE (req, res, next, xUID, doctorID) {
  Doctor.doctorsDoctorIDDELETE(xUID, doctorID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.doctorsDoctorIDGET = function doctorsDoctorIDGET (req, res, next, xUID, doctorID) {
  Doctor.doctorsDoctorIDGET(xUID, doctorID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.doctorsDoctorIDPUT = function doctorsDoctorIDPUT (req, res, next, body, doctorID, xUID) {
  Doctor.doctorsDoctorIDPUT(body, doctorID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.doctorsGET = function doctorsGET (req, res, next, doctorName, departmentID, hospitalName) {
  Doctor.doctorsGET(doctorName, departmentID, hospitalName)
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
