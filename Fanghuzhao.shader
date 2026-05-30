Shader "Unlit/Fanghuzhao"
{
    Properties
    {
        _center ("Zhongdian",Vector) = (0,0,0,0)
    }
    SubShader
    {
        Tags { "RenderType"="OTransparentpaque" "Queue" = "Transparent"}
        LOD 100

        Pass
        {
            ZWrite off
            Blend SrcAlpha OneMinusSrcAlpha

            HLSLPROGRAM
            #pragma vertex vert
            #pragma fragment frag
            // make fog work
            #pragma multi_compile_fog

            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/Core.hlsl"
            #include "Packages/com.unity.render-pipelines.core/Runtime/Utilities/Blit.hlsl"
            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/DeclareDepthTexture.hlsl"


            struct appdata
            {
                float4 vertex : POSITION;
                float3 normal : NORMAL;
                float2 uv : TEXCOORD0;
            };

            struct v2f
            {
                float4 vertex : SV_POSITION;

                float3 vertexWORLDS : TEXCOORD1;
                float3 cameraV : TEXCOORD2;
                float3 normal : NORMAL;

                float2 uv : TEXCOORD0;

                float4 jvlicenter : TEXCOORD3;
            };

            float4 _center;


            v2f vert (appdata v)
            {
                v2f o;
                o.vertexWORLDS= TransformObjectToWorld(v.vertex.xyz);
                o.normal = TransformObjectToWorldNormal(v.normal);
                o.cameraV = GetCameraPositionWS() - o.vertexWORLDS;
                o.uv = v.uv;


                o.jvlicenter = float4(_center.xyz - o.vertexWORLDS,1);


                float3 normalWorld = normalize(o.normal);
                float qifu = sin(o.vertexWORLDS.y * 60 + _Time.y * 5);
                float _Maxheight = 0.1;

                float right = 1;
                float ctrl = smoothstep(-right,right,qifu);

                o.vertexWORLDS = o.vertexWORLDS + normalWorld * _Maxheight * ctrl;
                o.vertex = TransformWorldToHClip(o.vertexWORLDS);

                return o;
            }

            half4 frag (v2f i) : SV_Target
            {
                float bylight = 1 - dot(normalize(i.normal),normalize(i.cameraV));
                bylight = bylight * bylight * 0.35;

                float pengzhuang = 1 / length(i.jvlicenter);
                pengzhuang = 4 * smoothstep(0.5,1,pengzhuang);

                half4 ret = half4(0,0,0,1) + pengzhuang * half4(1,1,1,1) + bylight * half4(1,1,1,1);
                ret.w = 0.5;

                return ret;
            }
            ENDHLSL
        }
    }
}
