const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

const locale = {
    ru: {
        months: [
            'Январь', 'Февраль', 'Март', 'Апрель',
            'Май', 'Июнь', 'Июль', 'Август',
            'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'
        ],
        dayOfWeekShort: [
            'Вс', 'Пн', 'Вт', 'Ср',
            'Чт', 'Пт', 'Сб'
        ]
    }
}

$('#startDate').datetimepicker({
    format: 'Y-m-d',
    timepicker: false,
    onChangeDateTime: function (current_date) {
        $('#endDate').datetimepicker("setOptions", {"minDate": current_date === null ? false : current_date});
    },
    i18n: locale
})

$('#endDate').datetimepicker({
    format: 'Y-m-d',
    timepicker: false,
    i18n: locale
})

$('#startTime').datetimepicker({
    format: 'H:i',
    datepicker: false,
    onChangeDateTime: function (current_time) {
        $('#endTime').datetimepicker("setOptions", {"minTime": current_time === null ? false : current_time});
    }
})

$('#endTime').datetimepicker({
    format: 'H:i',
    datepicker: false
})

$('#dateTime').datetimepicker({
    format: 'Y-m-d\\TH:i:s',
    lazyInit: true,
    i18n: locale
})

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data.substring(0, 16).replace("T", " ");
                        }
                        return data;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );
    $.datetimepicker.setLocale(navigator.language);
});