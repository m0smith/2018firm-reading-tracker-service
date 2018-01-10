all: src/main/webapp/index.html

test:
	planck -c src/main/cljc -m bofm-tracker.core

src/main/webapp/index.html: src/main/cljc/bofm_tracker/core.cljc
	planck -c src/main/cljc -m bofm-tracker.core > src/main/webapp/index.html

