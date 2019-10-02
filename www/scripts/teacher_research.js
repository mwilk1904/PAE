/**
 * Code JavaScript pour la page d'accueil du professeur / professeur responsable
 */

/**
 * Role des utilisateurs
 */
const role = {
    student: "Étudiant",
    teacher: "Professeur",
    resp_teacher: "Professeur responsable"
}

let studentId;
let studentName;
let dataTableStMob = [];
let dataTablePartners = {};
let dataTableUsers = {};

$(function() {
    /**
     * Recherche utilisateurs
     */
    $('a[aria-controls="research_t"]').on('show.bs.tab', function(e) {
        $('a[aria-controls="pills-users_t"]').click();
    });

    $('#table_users_t').on('click', '.promote_user', function() {
        let userId = {};
        let tr = $(this).closest('tr');
        let user_name = dataTableUsers.row(tr).data().firstName;
        userId['userId'] = parseInt(tr.find('.userId').text());

        alertify
            .confirm(
                "Promotion",
                "Êtes-vous sûr de vouloir promouvoir " + user_name + " en tant que professeur?",
                function() {
                    /**
                     * Promouvoir etudiant
                     */
                    $.post("/mobilite", {
                        action: "setTeacher",
                        data: JSON.stringify(userId)
                    }).fail(function(jqXHR, textStatus, error) {
                        const errorData = jqXHR.responseJSON;
                        console.log(errorData.message);
                    }).done(function(data) {
                        $.post("/mobilite", {
                            action: "getUsers"
                        }).fail(function(jqXHR, textStatus, error) {
                            const errorData = jqXHR.responseJSON;
                            console.log(errorData.message);
                        }).done(function(data) {
                            dataTableUsers = initUsersTable(data);
                            $('#table_users_t').css('width', '100%')
                        });
                    });
                    alertify.success("Vous avez promu " + user_name);
                },
                function() {
                    alertify.error("Promotion annulée");
                }
            )
            .set("labels", { ok: "Confirmer", cancel: "Annuler" });


    });
    /**
     * Retour details etudiant
     */
    $('#bt_back_student_details').on('click', function() {
        reset_student_pers_data();
        $('a[aria-controls=research_t]').click();
    });


    /**
     * Gestion du button Details pour un etudiant
     */
    $('#tb_students tbody').on('click', 'button', function() {
        studentId = $(this).parent().siblings().last().text();
        studentName = $(this).parent().siblings().eq(0).text() + ", " + $(this).parent().siblings().eq(1).text();

        let stId = {};
        stId['studentId'] = parseInt(studentId);
        $('#studentName').text(studentName);

        $('a[aria-controls=student_details]').click();

        /**
         * Recuperation des mobilités d'un étudiant
         */
        $.post("/mobilite", {
                action: "getStudentMobilities",
                data: JSON.stringify(stId)
            },
            function(resp) {
                selectedStudentMob = resp;
            }
        ).fail(function(jqXHR, textStatus, error) {
            const errorData = jqXHR.responseJSON;
            console.log(errorData.message);
        }).done(function(data) {

            let dataReceived = transformReceivedJSONMobilities(data);
            dataTableStMob = $('#tb_student_mobilities').DataTable({
                "language": {
                    sUrl: "French.json"
                },
                "destroy": true,
                "paging": false,
                "info": false,
                "searching": false,
                "aaData": dataReceived,
                "columns": [
                    { "data": "mobilityCode" },
                    { "data": "preferenceOrderNumber" },
                    { "data": "partner" },
                    { "data": "program" },
                    { "data": "countryName" },
                    { "data": "state" }
                ],
                order: [
                    [1, 'asc']
                ],
                createdRow: function(row, data, dataIndex) {
                    if (data.state === "Annulée") {
                        $(row).addClass("cancelledRow text-danger");
                    }
                }
            });
        });

        /**
         * Recuperation des données de l'étudiant
         */
        $.post("/mobilite", {
            action: "getStudentData",
            data: JSON.stringify(stId)
        }).fail(function(jqXHR, textStatus, error) {
            const errorData = jqXHR.responseJSON;
            console.log(errorData.message);
        }).done(function(data) {

            $.each(data, function(key, value) {
                if (value) {
                    if (key === "nationalityId") {
                        value = countryIdToCountry(value).countryName;

                    }
                    if (key === "birthDate") {
                        $('#st_' + key).text((new Date(value)).toLocaleDateString("fr-FR"));
                        return;
                    }
                    $('#st_' + key).text(value);
                }
            });
        });

    });

    /**
     * Recherche partenaires
     */
    $('a[aria-controls="pills-partners_t"]').on('shown.bs.tab', function(e) {
        if (!$.isEmptyObject(dataTablePartners)) {
            dataTablePartners.destroy();
        }
        dataTablePartners = initPartnersTable(tabPartners);

    });




    /**
     * Details de la table des partenaires
     */

    // Array to track the ids of the details displayed rows
    let detailRows = [];
    $('#table_partners_t tbody, #table_partners_s tbody').on('click', 'tr td', function() {
        var tr = $(this).closest('tr');
        var row = dataTablePartners.row(tr);
        var idx = $.inArray(tr.attr('id'), detailRows);

        if (row.child.isShown()) {
            tr.removeClass('details');
            row.child.hide();

            // Remove from the 'open' array
            detailRows.splice(idx, 1);
        } else {
            tr.addClass('details');
            row.child(formatTablePartners(row.data())).show();

            // Add to the 'open' array
            if (idx === -1) {
                detailRows.push(tr.attr('id'));
            }
        }
    });
});

function initPartnersTable(table) {
    return $('#table_partners_t, #table_partners_s').DataTable({
        "language": {
            sUrl: "French.json"
        },
        "destroy": true,
        "aaData": table,
        "columns": [{
                "className": 'details-control',
                "orderable": false,
                "data": null,
                "defaultContent": ''
            },
            { "data": "fullName" },
            { "data": "mobilityCode" },
            { "data": "address" },
            {
                "data": "countryId",
                render: function(data, type, row) {
                    if (data) {
                        return countryIdToCountry(data).countryName;
                    }
                }
            },
            { "data": "city" },
            { "data": "email" },
            { "data": "phoneNumber" }
        ]
    });
}

function formatTablePartners(d) {
    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
        '<tr>' +
        '<td>Nom légal:</td>' +
        '<td>' + d.legalName + '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>Nom d\'affaires:</td>' +
        '<td>' + d.businessName + '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>Site internet:</td>' +
        '<td>' + d.website + '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>Département:</td>' +
        '<td>' + d.department + '</td>' +
        '</tr>' +
        '</table>';
}

function initUsersTable(dataR) {
    return $('#table_users_t').DataTable({
        responsive: true,
        "language": {
            sUrl: "French.json"
        },
        "destroy": true,
        "info": false,
        "aaData": dataR,
        "columns": [
            { "data": "pseudo" },
            { "data": "lastName" },
            { "data": "firstName" },
            { "data": "email" },
            {
                "data": null,
                render: function(data, type, row) {
                    return role[data.role];
                }
            },
            {
                "data": null,
                render: function(data, type, row) {
                    if (whoami.role !== "resp_teacher") {
                        return "";
                    }
                    if (data.role === "student") {
                        return '<button type="button" class="btn btn-primary btn-sm promote_user">Attribuer le role de professeur</button>';
                    } else {
                        return '<button type="button" class="btn btn-primary btn-sm" disabled>Attribuer le role de professeur</button>';
                    }
                }
            },
            {
                "data": "id",
                "className": "d-none userId"
            }
        ]
    });
}

function reset_student_pers_data() {
    $('#tb_student_pers_data tr').each(function(index, element) {
        let td = $(this).children().eq(1);
        td.text("");
    });
}