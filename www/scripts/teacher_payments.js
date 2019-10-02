/**
 * Code JavaScript pour la page des paiements du professeur / professeur responsable
 */

function initPaymentsTab() {
    /**
     * Recuperation des paiements
     */
    $.post("/mobilite", {
        action: "getAllMobilitiesOverview"
    }).fail(function(jqXHR, textStatus, error) {
        const errorData = jqXHR.responseJSON;
        console.log(errorData.message);

    }).done(function(dataR) {
        for (let index = 0; index < dataR.length; index++) {
            dataR[index]['academicalYear'] = transformDateToAcademicYear(dataR[index].introductionDate)

        }

        $('#tb_payments').DataTable({
            //rowGroup: true,
            "language": {
                sUrl: "French.json"
            },
            "destroy": true,
            "info": false,
            "aaData": dataR,
            "columns": [
                { "data": "applicationNumber" },
                { "data": "firstName" },
                { "data": "lastName" },
                {
                    "data": "programId",
                    render: function(data, type, row) {
                        return programIdToProgram(data).programName;
                    }
                },
                {
                    "data": "firstPayReq",
                    render: function(data, type, row) {
                        if (data) {
                            return '<span class="text-success"> Payé </span>';
                        } else {
                            return '<span class="text-danger"> À payer </span>';
                        }
                    }
                },
                {
                    "data": "secondPayReq",
                    render: function(data, type, row) {
                        if (data) {
                            return '<span class="text-success"> Payé </span>';
                        } else {
                            return '<span class="text-danger"> À payer </span>';
                        }
                    }
                },
                { "data": "academicalYear" }

            ],
            rowGroup: {
                dataSrc: 'academicalYear',
                className: 'bg-dark text-white '
            },
            order: [
                [6, 'asc'],
                [0, 'asc']
            ]
        });
    });
}

function transformDateToAcademicYear(miliseconds) {
    let date = new Date(miliseconds);
    let month = date.getMonth();
    if (month >= 9) {
        return date.getFullYear() + "/ " + (date.getFullYear() + 1);
    } else {
        return (date.getFullYear() - 1) + "/ " + date.getFullYear();
    }

}