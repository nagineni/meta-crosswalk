DESCRIPTION = "A window manager experiment written in Javascript, HTML5 and CSS3."
HOMEPAGE = "http://www.rlamana.es/ventus"
LICENSE = "MIT"

RDEPENDS_${PN} = "\
    appshell \
    chrome-app-samples \
    hexgl \
    "

SRC_URI += "\
    git://github.com/rlamana/Ventus.git;rev=27ddc5e296d6f95737534abe2727e95a1a741b33 \
    file://appshell_wm.patch;patch=1 \
    file://init \
    file://ventus_appshell \
    file://ventus-appshell.service \
    file://manifest.json \
    file://main.js \
    "

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=cb22e397c3ec96cd37c14022da86a678"

inherit update-rc.d systemd

INITSCRIPT_NAME = "ventus-appshell-service"
INITSCRIPT_PARAMS = "start 06 5 2 3 . stop 22 0 1 6 ."

SYSTEMD_SERVICE_${PN} = "ventus-appshell.service"

do_compile() {
    # No-op. Do not run make!
    exit 0
}

do_install() {
    install -d ${D}${libdir}/ventus-appshell/
    cp -Pr ${S}/* ${D}${libdir}/ventus-appshell/
    install -m 0644 ${WORKDIR}/manifest.json ${D}${libdir}/ventus-appshell/
    install -m 0644 ${WORKDIR}/main.js ${D}${libdir}/ventus-appshell/

    install -d ${D}${bindir}/
    install -m 0755 ${WORKDIR}/ventus_appshell ${D}${bindir}/ventus_appshell

    if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
        install -d ${D}${sysconfdir}/init.d
        install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/ventus-appshell-service
    else
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/ventus-appshell.service ${D}${systemd_unitdir}/system
    fi
}


pkg_postinst_${PN} () {
    mkdir -p $D/${libdir}/ventus-appshell/calculator/
    cp -Pr $D/${libdir}/chrome-app-samples/calculator/* $D/${libdir}/ventus-appshell/calculator/

    mkdir -p $D/${libdir}/ventus-appshell/hello-world/
    cp -Pr $D/${libdir}/chrome-app-samples/hello-world/* $D/${libdir}/ventus-appshell/hello-world/

    mkdir -p $D/${libdir}/ventus-appshell/hexgl/
    cp -Pr $D/${libdir}/hexgl/* $D/${libdir}/ventus-appshell/hexgl/
}
