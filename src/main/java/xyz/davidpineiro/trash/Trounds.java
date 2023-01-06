package xyz.davidpineiro.trash;

// trash + sounds = trounds
//trash sounds, trounds even...
public final class Trounds{

    enum Other implements TroundTypeInterface{
        sussy_balls("suspicious:sussy.fort_balls1"),
        fortnite_abstract_balls("suspicious:sussy.fortnitesusballsabstract"),
        ;

        private final String songId;

        Other(String songId) {
            this.songId = songId;
        }

        @Override
        public String getSongID() {
            return this.songId;
        }
    }

    enum Music implements TroundTypeInterface{
        interstellar("suspicious:sussy.interstellar"),
        kendrick("suspicious:sussy.kendrick_adhd"),
        bjork("suspicious:sussy.bjork_sus1"),
        howls_castle("suspicious:sussy.howls_castle"),
        hotel_california("suspicious:sussy.hotelcalifornia"),
        rocket_man("suspicious:sussy.rocketman"),
        ;

        private final String songId;

        Music(String songId) {
            this.songId = songId;
        }

        @Override
        public String getSongID() {
            return this.songId;
        }
    }

    enum Sfx implements TroundTypeInterface{
        vine_boom("suspicious:sussy.sfx.vineboom"),
        vine_boom_low_quality("suspicious:sussy.sfx.vineboomlowquality"),
        wet_fart("suspicious:sussy.sfx.wetfart"),

        tc_activate("suspicious:sussy.transportchicken.user_activate"),
        tc_receive("suspicious:sussy.transportchicken.receiver_receive"),
        ;

        private final String songId;

        Sfx(String songId) {
            this.songId = songId;
        }

        @Override
        public String getSongID() {
            return this.songId;
        }
    }

}
