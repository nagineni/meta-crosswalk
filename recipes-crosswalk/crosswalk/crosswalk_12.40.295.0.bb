include crosswalk.inc

DESCRIPTION = "Crosswalk is a web runtime for ambitious HTML5 applications."
HOMEPAGE = "https://crosswalk-project.org/"

do_configure() {
    cd ${S}

    # Attempt to link on 32-bits systems.
    export LDFLAGS="${LDFLAGS} -Wl,--no-keep-memory"

    # Force the host compiler. When building for 64-bits target in a 64-bits host,
    # the tools built by Chromium (mostly generators) were not working because apparently
    # the target compiler is being used and the output is not always valid on the host.
    export CC_host="gcc"
    export CXX_host="g++"

    build/linux/unbundle/replace_gyp_files.py ${DEFAULT_CONFIGURATION}
    xwalk/tools/upstream_revision.py -r $(grep ^blink_upstream_rev xwalk/DEPS.xwalk |cut -d\' -f2) -o xwalk/build/UPSTREAM.blink
    xwalk/gyp_xwalk --depth=. ${DEFAULT_CONFIGURATION} -I${WORKDIR}/include.gypi
}

do_compile() {
    ninja ${PARALLEL_MAKE} -C ${S}/out/Release xwalk
}

do_install() {
    install -d ${D}${libdir}/xwalk/
    install -m 0755 ${S}/out/Release/xwalk ${D}${libdir}/xwalk/xwalk
    install -m 0644 ${S}/out/Release/icudtl.dat ${D}${libdir}/xwalk/icudtl.dat
    install -m 0644 ${S}/out/Release/libffmpegsumo.so ${D}${libdir}/xwalk/libffmpegsumo.so
    install -m 0644 ${S}/out/Release/xwalk.pak ${D}${libdir}/xwalk/xwalk.pak

    install -d ${D}${bindir}/
    ln -sf ${libdir}/xwalk/xwalk ${D}${bindir}/xwalk
}

FILES_${PN} = "${bindir}/xwalk ${libdir}/xwalk/*"
FILES_${PN}-dbg = "${bindir}/.debug/ ${libdir}/xwalk/.debug/"

PACKAGE_DEBUG_SPLIT_STYLE = "debug-without-src"
