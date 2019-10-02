/**
 * Code JavaScript pour la page d'accueil du professeur / professeur responsable
 */

let dataTableAllMobilities = {};
let tabStudents = {};
let tabCancellationReasons = {};
let teacherIsConnectedAsStudent = false;
let teacher = null;
let mobilityId = {};
let currentMobility = {};

$(function() {

    //$.fn.select2.defaults.set('language', $.fn.select2.amd.require("./scripts/select2.french"));

    /**
     * Deconnexion
     */
    $("#bt_logout_teacher").on("click", function() {
        if (!$.isEmptyObject(dataTableUsers)) {
            dataTableUsers.destroy();
        }
        $("#bt_logout_student").click(); //trigger du button de deconnexion

    });

    $("#tb_prior_requests, #tb_posterior_requests").on("click", ".confirm_document , .send_document", function() {
        if (currentMobility.state === "none") {
            alertify.error("Veuillez confirmer la mobilité au préalable");
            return false;
        }
        let boolean_go = $(this).closest('table').attr('id') === "tb_prior_requests";

        if ($(this).hasClass("confirm_document")) {
            if ($(this).hasClass("doc_back") && currentMobility.state !== "to_pay" && currentMobility.state !== "first_demand" && currentMobility.state !== "switzerland") {
                alertify.error('La mobilité doit passer à "À payer" ou "1ere demande de paiement" avant de remplir les documents postérieurs!');
                return false;
            }
            $(this).closest("td").empty().append(changeConfirmButton(true, boolean_go));
        } else {
            $(this).closest("td").empty().append(changeSendButton(true, boolean_go));
        }
    });

    /**
     * Confirmer un encodage 
     */
    $("#tb_encodings").on("click", ".confirm_encoding", function() {
        if (currentMobility.state === "none") {
            alertify.error("Veuillez confirmer la mobilité au préalable");
            return false;
        }
        let program = programIdToProgram(currentMobility.programId).programName;
        let id = $(this).closest('tr').find('th').attr('id');
        if (id === "mob") {
            if (program !== "FAME" || program !== "Erabel") {
                alertify.error("Mobi seulement pour le programme FAME ou Erabel");
                return false;
            }
        }
        if (id === "mobilityTool") {
            if (program !== "Erasmus+") {
                alertify.error("Mobility Tool seulement pour le programme Erasmus+");
                return false;
            }

        }
        $(this)
            .closest("td")
            .empty()
            .append(changeConfirmEncodingButton(true));
    });

    /**
     * Connexion en tant qu'étudiant
     */
    $("#bt_login_as").on("click", function() {
        if ($('#login_users').val().length === 0) {
            alertify.error("Veuillez selectionner un étudiant");
            return false;
        }
        let table = $('#login_users').val().split(" ");
        let userId = {};
        userId[table[0]] = parseInt(table[1]);
        /**
         * Recuperation des mobilités
         */
        $.post("/mobilite", {
                action: "loginAsStudent",
                data: JSON.stringify(userId)
            })
            .fail(function(jqXHR, textStatus, error) {
                const errorData = jqXHR.responseJSON;
                console.log(errorData.message);
            })
            .done(function(dataR) {
                let user = userIdToUser(parseInt(table[1]));
                alertify.success("Vous etes connecté en tant que " + user.pseudo);
                $('#bt_logout_student').text("Revenir");
                teacher = whoami;
                loggedOut();
                loggedIn_student(user);
                teacherIsConnectedAsStudent = true;
            });



    });
    /**
     * Export CSV
     */
    $("#bt_export_csv").on("click", function() {
        let dataTableOutput = dataTableAllMobilities
            .rows()
            .cells()
            .render("display")
            .toArray();
        let listOfMobilities = [];
        for (let i = 0; i < dataTableOutput.length; i += 12) {
            listOfMobilities.push({
                applicationNumber: dataTableOutput[i],
                firstName: dataTableOutput[i + 1],
                lastName: dataTableOutput[i + 2],
                preferenceOrderNumber: dataTableOutput[i + 3],
                mobilityCode: dataTableOutput[i + 4],
                partner: dataTableOutput[i + 6] ?
                    dataTableOutput[i + 6] + " (" + dataTableOutput[i + 7] + ")" : "",
                program: dataTableOutput[i + 8],
                department: "BIN",
                semester: dataTableOutput[i + 4] === "SMS" ? "Q1" : "Q2"
            });
        }
        $.post("/mobilite", {
                action: "generateCsv",
                data: JSON.stringify(listOfMobilities)
            })
            .fail(function(jqXHR, textStatus, error) {
                const errorData = jqXHR.responseJSON;
                console.log(errorData.message);
            })
            .done(function(data) {
                //data = "SEP=, \n" + data;
                let encodedUri = encodeURI(data);
                let link = document.createElement("a");
                link.setAttribute(
                    "href",
                    "data:attachment/csv;charset=UTF-8,%EF%BB%BF" + encodedUri
                );
                link.setAttribute("download", "liste_de_mobilities.csv");
                document.body.appendChild(link);
                link.click();
            });
    });

    /**
     * Double click sur une mobilitie
     */
    $("#tb_mobilities tbody").on("dblclick", "tr", function() {
        let currentRowData = dataTableAllMobilities.row(this).data();
        let state = currentRowData.state;
        if (state === "cancelled") {
            alertify.error("La mobilité est annulée!");
            return false;
        }
        let studentName = currentRowData.lastName + ", " + currentRowData.firstName;
        $("#span_name").empty().append(studentName);
        $("#span_state").empty().append(etatMob[currentRowData.state]);
        $("#span_program").empty().append(programIdToProgram(currentRowData.programId).programName);
        let mob = currentRowData.mobId;
        $("#span_mobility_num").empty().append(mob);
        let mobId = {};
        mobId["mobilityId"] = parseInt(mob);

        $.post("/mobilite", {
                action: "getMobility",
                data: JSON.stringify(mobId)
            })
            .fail(function(jqXHR, textStatus, error) {
                const errorData = jqXHR.responseJSON;
                console.log(errorData.message);
            })
            .done(function(data) {
                currentMobility = data;
                initMobilityDocuments(data);
            });

        $("a[aria-controls=mobility_details]").click();
    });

    $("a[aria-controls=mobility_details]").on('hide.bs.tab', function() {
        setTimeout(function() {
            ajaxCallsTeacherConnection();
        }, 1000);
    });

    /**
     * Sauver la mobilité
     */
    $('#bt_save_mobility').on('click', function() {
        let state = currentMobility.state;

        if (state === 'none') {
            alertify.error("Veuillez confirmer la mobilitié au préalable");
            return false;
        }

        saveMobilityDocuments(currentMobility);
        $.post("/mobilite", {
            action: "updateMobility",
            data: JSON.stringify(currentMobility)
        }).fail(function(jqXHR, textStatus, error) {
            const errorData = jqXHR.responseJSON;
            console.log(errorData.message);
        }).done(function() {
            alertify.success("Vous avez sauvé les changements");
            /**
             * Mise a jour de l'etat
             */
            setTimeout(function() {
                let id = {};
                id['mobilityId'] = parseInt(currentMobility.id);
                $.post("/mobilite", {
                        action: "getMobility",
                        data: JSON.stringify(id)
                    })
                    .fail(function(jqXHR, textStatus, error) {
                        const errorData = jqXHR.responseJSON;
                        console.log(errorData.message);
                    })
                    .done(function(data) {
                        $("#span_state").empty().append(etatMob[data.state]);
                    });
            }, 2000);

        });
    });

    /**
     * Confirmer une mobilitie
     */
    $("#tb_mobilities tbody").on("click", ".toConfirm", function() {
        let tr = $(this).closest("tr")[0];
        let currentRowData = dataTableAllMobilities.row(tr).data();

        let mob = currentRowData.mobId;
        let mobId = {};
        mobId["mobilityId"] = parseInt(mob);

        alertify
            .confirm(
                "Confirmation",
                "Êtes-vous sûr de vouloir confirmer cette mobilitié?",
                function() {
                    $.post("/mobilite", {
                            action: "confirmMobility",
                            data: JSON.stringify(mobId)
                        })
                        .fail(function(jqXHR, textStatus, error) {
                            const errorData = jqXHR.responseJSON;
                            console.log(errorData.message);
                        })
                        .done(function() {
                            dataTableAllMobilities.destroy();
                            ajaxCallsTeacherConnection();
                        });
                    alertify.success("Mobilité confirmée!");
                },
                function() {
                    alertify.error("Confirmation annulée");
                }
            )
            .set("labels", { ok: "Confirmer", cancel: "Annuler" });
    });

    /**
     * Annuler une mobilitié
     */
    $("#tb_mobilities tbody").on("click", ".toCancel", function() {
        let tr = $(this).closest("tr")[0];
        let currentRowData = dataTableAllMobilities.row(tr).data();

        let mob = currentRowData.mobId;
        mobilityId["mobilityId"] = parseInt(mob);
    });

    $('#bt_confirm_modal').on('click', function() {
        const reason = $("#cancellation_reasons")[0].value;
        mobilityId["reason"] = reason;
        $.post("/mobilite", {
            action: "cancelMobility",
            data: JSON.stringify(mobilityId)
        }).fail(function(jqXHR, textStatus, error) {
            const errorData = jqXHR.responseJSON;
            console.log(errorData.message);
        }).done(function() {
            ajaxCallsTeacherConnection();
            alertify.success("Vous avez annulé cette mobilité");
            $("#cancellation_reasons").select2('destroy');
            $("#cancellation_reasons").html('<option value="" disabled hidden></option>');
            $("#cancellation_reasons").select2();
            $('#bt_confirm_modal').modal('hide');
        });
        $('#bt_back_modal').click();
    });

    $("#bt_back_mobility_details").on("click", function() {
        currentMobility = {};
        ajaxCallsTeacherConnection();

        $("a[aria-controls=home_t]").click();
    });
});

function initAllMobilitiesTable(dataReceived) {
    return $("#tb_mobilities").DataTable({
        scrollX: false,
        reponsive: true,
        language: {
            sUrl: "French.json"
        },
        destroy: true,
        info: false,
        aaData: dataReceived,
        columns: [
            { data: "applicationNumber", className: "d-none " },
            { data: "firstName" },
            { data: "lastName" },
            { data: "preferenceOrderNumber" },
            { data: "mobilityCode" },
            {
                data: "state",
                render: function(data, type, row) {
                    return etatMob[data];
                }
            },
            {
                data: "partnerId",
                render: function(data, type, row) {
                    if (partnerIdToPartner(data) === undefined) {
                        return "";
                    }
                    return partnerIdToPartner(data).fullName;
                }
            },
            {
                data: "countryId",
                render: function(data, type, row) {
                    if (countryIdToCountry(data) === undefined) {
                        return "";
                    }
                    return countryIdToCountry(data).countryName;
                }
            },
            {
                data: "programId",
                render: function(data, type, row) {
                    if (programIdToProgram(data) === undefined) {
                        return "";
                    }
                    return programIdToProgram(data).programName;
                }
            },
            {
                data: null,
                render: function(data, type, row) {
                    if (row.state === "cancelled") {
                        return '<button type="button" class="btn btn-primary btn-sm" disabled>Annulée</button>';
                    } else if (row.confirmed) {
                        return '<button type="button" class="btn btn-primary btn-sm" disabled>Confirmée</button>';
                    } else {
                        return '<button type="button" class="btn btn-primary btn-sm toConfirm" >Confirmer</button>';
                    }
                }
            },
            {
                data: null,
                render: function(data, type, row) {
                    if (row.state === "cancelled") {
                        return '<button type="button" class="btn btn-danger btn-sm" disabled>Annulée</button>';
                    }
                    return '<button type="button" class="btn btn-danger btn-sm toCancel" data-toggle="modal" data-target="#cancelModal">Annuler</button>';
                }
            },
            { data: "mobId", className: "d-none mobId" }
        ],
        rowGroup: {
            dataSrc: "lastName",
            className: "bg-dark text-white "
        },
        order: [
            [2, "asc"],
            [3, "asc"]
        ],
        createdRow: function(row, data, dataIndex) {
            if (data.state === "cancelled") {
                $(row).addClass("cancelledRow text-danger");
            }
        }
    });


}

function ajaxCallsTeacherConnection() {
    /**
     * Get cancellation reasons
     */
    $.post("/mobilite", {
            action: "getCancellationReasons"
        },
        function(resp) {
            tabCancellationReasons = resp;
        }).fail(function(jqXHR, textStatus, error) {
        const errorData = jqXHR.responseJSON;
        console.log(errorData.message);
    }).done(function(data) {});

    /**
     * Recuperation des users pour le select
     */
    $.post("/mobilite", {
            action: "getAllStudents"
        })
        .fail(function(jqXHR, textStatus, error) {
            const errorData = jqXHR.responseJSON;
            console.log(errorData.message);
        })
        .done(function(dataR) {
            tabStudents = dataR;
        });
    /**
     * Recuperation des mobilités
     */
    $.post("/mobilite", {
            action: "getAllMobilitiesOverview"
        })
        .fail(function(jqXHR, textStatus, error) {
            const errorData = jqXHR.responseJSON;
            console.log(errorData.message);
        })
        .done(function(dataR) {
            setTimeout(function() {
                dataTableAllMobilities = initAllMobilitiesTable(dataR);
            }, 1000);
        });

    /**
     * Recuperation du tableau des étudiants
     */
    $.post("/mobilite", {
            action: "getMobilityPostulants"
        })
        .fail(function(jqXHR, textStatus, error) {
            const errorData = jqXHR.responseJSON;
            console.log(errorData.message);
        })
        .done(function(data) {
            if (data.length === 0) {} else {
                $("#tb_students").DataTable({
                    language: {
                        sUrl: "French.json"
                    },
                    destroy: true,
                    info: false,
                    aaData: data,
                    columns: [
                        { data: "lastName" },
                        { data: "firstName" },
                        { data: "mobilitiesCount" },
                        { data: "unconfirmedMobilitiesCount" },
                        { data: "studentId", className: "d-none stuId" },
                        {
                            data: null,
                            defaultContent: '<button type="button" class="btn btn-info stuDetails">Détails <i class="fas fa-info-circle"></i></button>'
                        }
                    ]
                });
            }
        });

    /**
     * Recuperation des utilisateurs
     */
    $.post("/mobilite", {
            action: "getUsers"
        },
        function(resp) {}
    ).fail(function(jqXHR, textStatus, error) {
        const errorData = jqXHR.responseJSON;
        console.log(errorData.message);
    }).done(function(data) {
        dataTableUsers = initUsersTable(data);
        setTimeout(function() {
            $('#table_users_t').css('width', "100%");
        }, 500);
    });


    setTimeout(function() {
        populateUsersSelect();
        populateSelectCancellationReasons();
        initPaymentsTab();
    }, 2000);
}

/**
 * Recupere les documents confirmés
 */
function saveMobilityDocuments(mobility) {
    $("#tb_prior_requests tr, #tb_posterior_requests tr, #tb_encodings tr").each(
        function() {
            let id = $(this).find("th").attr("id");
            let boolean = $(this).find('button').is(':disabled');
            mobility[id] = boolean;
        }
    );
    if ($('#final_rapport').val() !== "") {
        mobility["finalReport"] = $('#final_rapport').val();
    }
}
/**
 * Initialise les documents deja recus pour une mobilite
 */
function initMobilityDocuments(mobility) {
    let program = programIdToProgram(currentMobility.programId).programName;
    $("#tb_prior_requests tr, #tb_posterior_requests tr, #tb_encodings tr").each(
        function() {
            let id = $(this).find("th").attr("id");
            let boolean = mobility[id];
            let boolean_go = $(this).closest('table').attr('id') === "tb_prior_requests";
            if ($(this).find("button").hasClass("confirm_document")) {
                if (id === "passingLangTestProofBack" || id === "passingLangTestProofGo") {
                    if (program !== "Erasmus+") {
                        $(this).find("td").empty().append('<button type="button" class="btn btn-primary btn-sm confirm_document" disabled>Document non nécessaire</button>');
                        return;
                    }
                }
                if (id === "scholarshipContract" && program === "Suisse") {
                    $(this).find("td").empty().append('<button type="button" class="btn btn-primary btn-sm confirm_document" disabled>Bourse gérée par la Suisse</button>');
                    return;
                }
                $(this).find("td").empty().append(changeConfirmButton(boolean, boolean_go));
            } else if ($(this).find("button").hasClass("send_document")) {
                if (program === "Suisse") {
                    $(this).find("td").empty().append('<button type="button" class="btn btn-success btn-sm send_document send_document" disabled>Le dossier est géré par la Suisse <i class="fas fa-check-circle"></i></button>');
                    return;
                }
                $(this).find("td").empty().append(changeSendButton(boolean, boolean_go));
            } else if ($(this).find("button").hasClass("confirm_encoding")) {
                $(this).find("td").empty().append(changeConfirmEncodingButton(boolean, boolean_go));
            }
        }
    );
    if (mobility.finalReport !== null) {
        $('#final_rapport').text(mobility.finalReport);
    }

}

function changeConfirmButton(disabled, doc_go) {
    if (!disabled) {
        if (doc_go) {
            return '<button type="button" class="btn btn-primary btn-sm confirm_document doc_go">Confirmer réception</button>';
        } else {
            return '<button type="button" class="btn btn-primary btn-sm confirm_document doc_back">Confirmer réception</button>';
        }
    } else {
        return '<button type="button" class="btn btn-primary btn-sm confirm_document" disabled>Réception confirmée <i class="fas fa-check-circle"></i></button>';
    }
}

function changeSendButton(disabled, doc_send_go) {
    if (!disabled) {
        if (doc_send_go) {
            return '<button type="button" class="btn btn-success btn-sm send_document doc_send_go" >Dossier envoyé à l\'HE</button>';
        } else {
            return '<button type="button" class="btn btn-success btn-sm send_document doc_send_back" >Dossier envoyé à l\'HE</button>';
        }
    } else {
        return '<button type="button" class="btn btn-success btn-sm send_document send_document" disabled>Le dossier a été envoyé <i class="fas fa-check-circle"></i></button>';
    }
}

function changeConfirmEncodingButton(disabled) {
    if (!disabled) {
        return '<button type="button" class="btn btn-primary btn-sm btn-block confirm_encoding">Confirmer</button>';
    } else {
        return '<button type="button" class="btn btn-primary btn-sm btn-block confirm_encoding " disabled>Confirmé <i class="fas fa-check-circle"></i></button>';
    }
}

function userIdToUser(userId) {
    return tabStudents.find(function(e) {
        return e.id === userId;
    });
}

function populateSelectCancellationReasons() {
    setTimeout(function() {
        $('#cancellation_reasons').select2({
            language: {
                noResults: function(params) {
                    return "Aucune raison d'annulation disponible.";
                },
                maximumSelected: function(e) {
                    return "Vous ne pouvez selectionner qu'une seule raison";
                }
            },
            placeholder: "Raison de l'annulation",
            tags: true,
            maximumSelectionLength: 1
        });
    }, 500);

    if ($('#cancellation_reasons option').length === tabCancellationReasons.length + 1) {
        return;
    }
    for (let i = 0; i < tabCancellationReasons.length; i++) {
        if (tabCancellationReasons[i] === null) {
            continue;
        }
        $('#cancellation_reasons').append($('<option>', {
            text: tabCancellationReasons[i],
            value: tabCancellationReasons[i]
        }));
    }


}

function populateUsersSelect() {
    if ($('#login_users option').length === tabStudents.length + 1) {
        return;
    }
    for (let i = 0; i < tabStudents.length; i++) {
        $('#login_users').append($('<option>', {
            text: tabStudents[i].firstName + ", " + tabStudents[i].lastName,
            value: "userId " + tabStudents[i].id
        }));
    }

    /*setTimeout(function() {
        $('#login_users').select2({
            placeholder: "Etudiant",
            closeOnSelect: true
        });
    }, 500);*/
}