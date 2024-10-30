'use strict';


/**
 * 診療科一覧取得
 * 診療科一覧を取得します
 *
 * xUID String 
 * returns inline_response_200_4
 **/
exports.departmentsGET = function(xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "departmentName" : "内科",
    "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
  } ]
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

