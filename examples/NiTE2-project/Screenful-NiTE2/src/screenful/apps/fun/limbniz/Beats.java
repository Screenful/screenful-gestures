package screenful.apps.fun.limbniz;

/**
 * Byte beats and assorted things. Needs cleanup and the parameters are quite
 * arbitrary.
 */
public class Beats {

    // return a tenth and wrap to byte
    private static int val(int orig) {
        return (int) (orig / 10) % 0xff;
    }

    private static int oneIfZero(int orig) {
        if (orig == 0) {
            return 1;
        } else {
            return orig;
        }
    }

    public static class Blippy implements Beat<Integer, BeatParams> {

        @Override
        public Integer apply(BeatParams param) {
            int aparam = oneIfZero(val((int) (3 + param.a)));

            int bparam = oneIfZero(val((int) (14 + param.b)));

            int cparam = oneIfZero(val((int) (4096 + param.c)));
            int u = (aparam) * param.t >> param.t / (cparam) % 4 & -param.t % (param.t >> 16 | 16) * param.t >> (bparam) & 8191;
            u = u / (u >> 4 | 1) * 4;
            return u;
        }
    }

    public static class HardAlgo implements Beat<Integer, BeatParams> {

        @Override
        public Integer apply(BeatParams param) {
            int aparam = param.a;
            int bparam = oneIfZero(15000 + (1000 * param.b));
            int cparam = oneIfZero(8 + param.c);
            int v = (2 | ((param.t >> 2) + aparam) + param.t % bparam) / cparam + 30;
            return v * (v % 232);
        }

    }

    public static class Synth implements Beat<Integer, BeatParams> {

        @Override
        public Integer apply(BeatParams param) {
            int v = param.t * ((param.a * 10) / (16 + param.a)) | (param.t >> ((param.b * 10) / 32 + param.c));
            return v;
        }

    }

    public static class Darkbeat implements Beat<Integer, BeatParams> {

        @Override
        public Integer apply(BeatParams param) {
            int aparam = 3 + param.a;
            int bparam = 90 + param.b;
            int cparam = 9 + param.c;
            return param.t >> param.t * param.t * aparam & param.t * 2 + (param.t >> bparam & 16) * 2 ^ (param.t >> 2) / 4 & cparam * param.t;
        }

    }

    public static class GlitchCore implements Beat<Integer, BeatParams> {

        @Override
        public Integer apply(BeatParams param) {
            int r = 2;
            int v = r;
            int aparam = 95 + param.a;
            int bparam = 65 + param.a;

            try {
                v = (v | ((param.t >> 2) + 1) + param.t % (aparam << 3)) / (((bparam << 3) + 1) / (aparam + 1)) + (bparam);
            } catch (ArithmeticException e) {
                return 1;
            }

            v = v * (v % 40);
            return v;
        }

    }

    public static class SimpleSound implements Beat<Integer, BeatParams> {

        @Override
        public Integer apply(BeatParams param) {
            int t = param.t;
            return t * (t ^ (t + param.b) + (t >> 15 | 1) ^ (t - (1280 + 64 * param.c) ^ t) >> (10 + param.a));
        }

    }

}
