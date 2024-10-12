'use strict';

var utils = require('../utils/writer.js');
var Medicine = require('../service/MedicineService');

module.exports.medicine_remindersGET = function medicine_remindersGET (req, res, next, xUID) {
  Medicine.medicine_remindersGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.medicine_remindersPOST = function medicine_remindersPOST (req, res, next, body, xUID) {
  Medicine.medicine_remindersPOST(body, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.medicine_remindersReminderIDDELETE = function medicine_remindersReminderIDDELETE (req, res, next, xUID, reminderID) {
  Medicine.medicine_remindersReminderIDDELETE(xUID, reminderID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

module.exports.medicine_remindersReminderIDPUT = function medicine_remindersReminderIDPUT (req, res, next, body, reminderID, xUID) {
  Medicine.medicine_remindersReminderIDPUT(body, reminderID, xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};

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
