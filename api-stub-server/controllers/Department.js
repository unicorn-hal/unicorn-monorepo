'use strict';

var utils = require('../utils/writer.js');
var Department = require('../service/DepartmentService');

module.exports.departmentsGET = function departmentsGET (req, res, next, xUID) {
  Department.departmentsGET(xUID)
    .then(function (response) {
      utils.writeJson(res, response);
    })
    .catch(function (response) {
      utils.writeJson(res, response);
    });
};
