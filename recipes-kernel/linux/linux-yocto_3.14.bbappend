FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

KBRANCH_genericx86  = "standard/common-pc/base"
KBRANCH_genericx86-64  = "standard/common-pc-64/base"

KMACHINE_genericx86 ?= "common-pc"
KMACHINE_genericx86-64 ?= "common-pc-64"

COMPATIBLE_MACHINE_genericx86 = "genericx86"
COMPATIBLE_MACHINE_genericx86-64 = "genericx86-64"

SRC_URI += "file://custom.cfg"

KERNEL_MODULE_AUTOLOAD += "igb"
KERNEL_MODULE_AUTOLOAD += "hid-multitouch"
KERNEL_MODULE_AUTOLOAD += "hid"
KERNEL_MODULE_AUTOLOAD += "hid-generic"
