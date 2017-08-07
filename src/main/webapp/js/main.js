$(function() {
    $("a[href^='#']").on('click', function(e) {
        if (!$(this.hash).offset()) {
            return;
        }
        e.preventDefault();
        var hash = this.hash;
        $('html, body').animate({
            scrollTop: $(this.hash).offset().top - 70
        }, 300, function(){
            window.location.hash = hash;
        });
    });
});

window.eddb = window.eddb ? window.eddb : {
    state: {

    }
};


window.eddb.system = {
    gotoId: function(id) {
        window.location = "/system/" + id;
    }
};

window.eddb.station = {
    gotoId: function(id) {
        window.location = "/station/" + id;
    }
};

window.eddb.body = {
    gotoId: function(id) {
        window.location = "/body/" + id;
    }
};

window.eddb.minorFaction = {
    gotoId: function(id) {
        window.location = "/faction/" + id;
    }
};


window.eddb.getFormValue = function (form, attribute) {

    var findInput = function (form, attribute) {
        var input = form.find(attribute.input);
        if (input.length && input[0].tagName.toLowerCase() === 'div') {
            // checkbox list or radio list
            return input.find('input');
        } else {
            return input;
        }
    };

    var input = findInput(form, attribute);
    var type = input.prop('type');
    if (type === 'checkbox' || type === 'radio') {
        var realInput = input.filter(':checked');
        if (!realInput.length) {
            realInput = form.find('input[type=hidden][name="' + input.prop('name') + '"]');
        }
        return realInput.val();
    } else {
        return input.val();
    }
};

$(function() {
    "use strict";
    window.setTimeout(function() {
        $('.opacity-fader').each(function (index, element) {
            $(element).addClass('opacity-fader-ready')
        });
    }, 1);

    $(function() {
        $("a[href*=\\#]:not([href=\\#])").click(function() {
            if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
                if (target.length) {
                    $('html,body').animate({
                        scrollTop: target.offset().top - 60
                    }, 1000);
                    return false;
                }
            }
        });
    });
});


$('body').on('click', function (e) {
    $('.btn-help.help-popover').each(function () {
        //the 'is' for buttons that trigger popups
        //the 'has' for icons within a button that triggers a popup
        if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
            $(this).popover('hide');
        }
    });
});