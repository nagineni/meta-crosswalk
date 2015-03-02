DESCRIPTION = "Official samples for Chrome Apps platform."
HOMEPAGE = "https://github.com/GoogleChrome/chrome-app-samples"
LICENSE = "Apache-2.0"

RDEPENDS_${PN} = "appshell"

SRC_URI += "git://github.com/GoogleChrome/chrome-app-samples.git;rev=f23edb5f2db7de6a0a3ec0e53cd1646003a93452 \
    file://calculator.desktop \
    file://calculator"

S = "${WORKDIR}/git"

LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

do_install() {
    install -d ${D}${libdir}/chrome-app-samples/
    cp -Pr ${S}/samples/* ${D}${libdir}/chrome-app-samples/

    install -d ${D}${bindir}/
    install -m 0755 ${WORKDIR}/calculator ${D}${bindir}/calculator

    install -d ${D}${datadir}/applications/
    install -m 0644 ${WORKDIR}/calculator.desktop ${D}${datadir}/applications/calculator.desktop
}

FILES_${PN} = "${bindir}/calculator ${datadir}/applications/* ${libdir}/chrome-app-samples/*"
