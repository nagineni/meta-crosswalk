require recipes-crosswalk/crosswalk/crosswalk.inc

DESCRIPTION = "app_shell is an experimental project to build a minimal environment like content_shell."

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
    build/gyp_chromium --depth=. ${DEFAULT_CONFIGURATION} -I${WORKDIR}/include.gypi
}

do_compile() {
    ninja ${PARALLEL_MAKE} -C ${S}/out/Release app_shell
}

do_install() {
    install -d ${D}${libdir}/appshell/
    install -m 0755 ${S}/out/Release/app_shell ${D}${libdir}/appshell/app_shell
    install -m 0644 ${S}/out/Release/icudtl.dat ${D}${libdir}/appshell/icudtl.dat
    install -m 0644 ${S}/out/Release/libffmpegsumo.so ${D}${libdir}/appshell/libffmpegsumo.so
    install -m 0644 ${S}/out/Release/extensions_shell_and_test.pak ${D}${libdir}/appshell/extensions_shell_and_test.pak

    install -d ${D}${bindir}/
    ln -sf ${libdir}/appshell/app_shell ${D}${bindir}/app_shell
}

FILES_${PN} = "${bindir}/app_shell ${libdir}/appshell/*"
FILES_${PN}-dbg = "${bindir}/.debug/ ${libdir}/appshell/.debug/"

PACKAGE_DEBUG_SPLIT_STYLE = "debug-without-src"
