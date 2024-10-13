'use strict';


/**
 * 医師削除
 * 医師を削除します
 *
 * xUID String 
 * doctorID String 
 * no response value expected for this operation
 **/
exports.doctorsDoctorIDDELETE = function(xUID,doctorID) {
  return new Promise(function(resolve, reject) {
    resolve();
  });
}


/**
 * 医師情報取得
 * 医師情報を取得します
 *
 * xUID String 
 * doctorID String 
 * returns Doctor
 **/
exports.doctorsDoctorIDGET = function(xUID,doctorID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "firstName" : "太郎",
  "lastName" : "山田",
  "chatSupportHours" : "9:00-17:00",
  "phoneNumber" : "090-1234-5678",
  "callSupportHours" : "9:00-17:00",
  "doctorID" : "1234567890",
  "departments" : [ {
    "departmentName" : "内科",
    "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
  }, {
    "departmentName" : "内科",
    "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
  } ],
  "hospital" : {
    "hospitalID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "hospitalName" : "東京都立大学病院"
  },
  "doctorIcon" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
  "email" : "sample@mail.com"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 医師情報更新
 * 医師情報を更新します
 *
 * body DoctorRequest 
 * doctorID String 
 * xUID String 
 * returns DoctorResponse
 **/
exports.doctorsDoctorIDPUT = function(body,doctorID,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "doctorName" : "山田 太郎",
  "phoneNumber" : "090-1234-5678",
  "hospitalID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "chatSupportEndHour" : "17:00",
  "callSupportStartHour" : "9:00",
  "departments" : [ "f47ac10b-58cc-4372-a567-0e02b2c3d479", "f47ac10b-58cc-4372-a567-0e02b2c3d479" ],
  "chatSupportStartHour" : "9:00",
  "callSupportEndHour" : "17:00",
  "doctorIcon" : "",
  "email" : "sample@mail.com"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 医師検索
 * 医者名、診療科、病院名で医師を検索します。全ての条件に一致する医師を返します（AND検索）。
 *
 * doctorName String 医者名の部分一致検索 (optional)
 * departmentID String 診療科IDで検索 (optional)
 * hospitalName String 病院名の部分一致検索 (optional)
 * returns inline_response_200
 **/
exports.doctorsGET = function(doctorName,departmentID,hospitalName) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "firstName" : "太郎",
    "lastName" : "山田",
    "chatSupportHours" : "9:00-17:00",
    "phoneNumber" : "090-1234-5678",
    "callSupportHours" : "9:00-17:00",
    "doctorID" : "1234567890",
    "departments" : [ {
      "departmentName" : "内科",
      "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    }, {
      "departmentName" : "内科",
      "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    } ],
    "hospital" : {
      "hospitalID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "hospitalName" : "東京都立大学病院"
    },
    "doctorIcon" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
    "email" : "sample@mail.com"
  }, {
    "firstName" : "太郎",
    "lastName" : "山田",
    "chatSupportHours" : "9:00-17:00",
    "phoneNumber" : "090-1234-5678",
    "callSupportHours" : "9:00-17:00",
    "doctorID" : "1234567890",
    "departments" : [ {
      "departmentName" : "内科",
      "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    }, {
      "departmentName" : "内科",
      "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    } ],
    "hospital" : {
      "hospitalID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "hospitalName" : "東京都立大学病院"
    },
    "doctorIcon" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
    "email" : "sample@mail.com"
  } ]
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 医師登録
 * 医師を登録します
 *
 * body DoctorRequest 
 * xUID String 
 * returns DoctorResponse
 **/
exports.doctorsPOST = function(body,xUID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "doctorName" : "山田 太郎",
  "phoneNumber" : "090-1234-5678",
  "hospitalID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "chatSupportEndHour" : "17:00",
  "callSupportStartHour" : "9:00",
  "departments" : [ "f47ac10b-58cc-4372-a567-0e02b2c3d479", "f47ac10b-58cc-4372-a567-0e02b2c3d479" ],
  "chatSupportStartHour" : "9:00",
  "callSupportEndHour" : "17:00",
  "doctorIcon" : "",
  "email" : "sample@mail.com"
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}


/**
 * 病院ごとの医師一覧取得
 * 病院の医師一覧を取得します
 *
 * xUID String 
 * hospitalID String 
 * returns inline_response_200
 **/
exports.hospitalsHospitalIDDoctorsGET = function(xUID,hospitalID) {
  return new Promise(function(resolve, reject) {
    var examples = {};
    examples['application/json'] = {
  "data" : [ {
    "firstName" : "太郎",
    "lastName" : "山田",
    "chatSupportHours" : "9:00-17:00",
    "phoneNumber" : "090-1234-5678",
    "callSupportHours" : "9:00-17:00",
    "doctorID" : "1234567890",
    "departments" : [ {
      "departmentName" : "内科",
      "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    }, {
      "departmentName" : "内科",
      "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    } ],
    "hospital" : {
      "hospitalID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "hospitalName" : "東京都立大学病院"
    },
    "doctorIcon" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
    "email" : "sample@mail.com"
  }, {
    "firstName" : "太郎",
    "lastName" : "山田",
    "chatSupportHours" : "9:00-17:00",
    "phoneNumber" : "090-1234-5678",
    "callSupportHours" : "9:00-17:00",
    "doctorID" : "1234567890",
    "departments" : [ {
      "departmentName" : "内科",
      "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    }, {
      "departmentName" : "内科",
      "departmentID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479"
    } ],
    "hospital" : {
      "hospitalID" : "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "hospitalName" : "東京都立大学病院"
    },
    "doctorIcon" : "VkJPUncwS0dnb0FBQUFOU1VoRVVnQUFBQmdBQUFBWUNBWUFBQURnZHozNEFBQUJqRWxFUVZSSVMrMlZ2VW9EUVJTR3Y3VlE=",
    "email" : "sample@mail.com"
  } ]
};
    if (Object.keys(examples).length > 0) {
      resolve(examples[Object.keys(examples)[0]]);
    } else {
      resolve();
    }
  });
}

