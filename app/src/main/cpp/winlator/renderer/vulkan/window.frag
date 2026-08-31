#version 450
layout(binding = 0) uniform sampler2D texSampler;
layout(push_constant) uniform PC {
    float ndcX0;
    float ndcY0;
    float ndcX1;
    float ndcY1;
    int   useTexAlpha;
    layout(offset = 20) float redMul;
    layout(offset = 24) float greenMul;
    layout(offset = 28) float blueMul;
    layout(offset = 32) int   useBalance;
} pc;
layout(location = 0) in vec2 fragTexCoord;
layout(location = 0) out vec4 outColor;
void main() {
    vec4 c = texture(texSampler, fragTexCoord);

    vec3 rgb = c.rgb;
    if (pc.useBalance != 0) {
        rgb *= vec3(pc.redMul, pc.greenMul, pc.blueMul);
    }

    outColor = vec4(rgb, pc.useTexAlpha != 0 ? c.a : 1.0);
}
