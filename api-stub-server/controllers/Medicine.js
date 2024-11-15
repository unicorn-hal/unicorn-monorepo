'use strict';

var utils = require('../utils/writer.js');
var Medicine = require('../service/MedicineService');

module.exports.medicinesGET = function medicinesGET (req, res, next, xUID) {
  Medicine.medicinesGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.medicinesMedicineIDDELETE = function medicinesMedicineIDDELETE (req, res, next, xUID, medicineID) {
  Medicine.medicinesMedicineIDDELETE(xUID, medicineID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.medicinesMedicineIDPUT = function medicinesMedicineIDPUT (req, res, next, body, medicineID, xUID) {
  Medicine.medicinesMedicineIDPUT(body, medicineID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.medicinesPOST = function medicinesPOST (req, res, next, body, xUID) {
  Medicine.medicinesPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.medicinesRemindersGET = function medicinesRemindersGET (req, res, next, xUID) {
  Medicine.medicinesRemindersGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
