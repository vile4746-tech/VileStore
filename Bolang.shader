Shader "Unlit/Bolang"
{
    Properties
    {
        _amp ("amp",range(0,100)) = 10
        _has ("has",range(0,10)) = 4
        _speed ("speed",range(0,10)) = 1
    }
    SubShader
    {
        Tags { "RenderType" = "Opaque" "RenderPipeline" = "UniversalPipeline" }
        LOD 100

        Pass
        {
            HLSLPROGRAM
            #pragma vertex vert
            #pragma fragment frag
            // make fog work
            #pragma multi_compile_fog

            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/Core.hlsl"
            #include "Packages/com.unity.render-pipelines.core/Runtime/Utilities/Blit.hlsl"

            struct appdata
            {
                float4 vertex : POSITION;
                float3 normal : NORMAL;
            };

            struct v2f
            {
                float4 vertex : SV_POSITION;
            };

            float _amp;
            float _has;
            float _speed;

            v2f vert (appdata v)
            {
                v2f o;
                float3 WorldVertex = TransformObjectToWorld(v.vertex.xyz);

                float3 normal = normalize(TransformObjectToWorldNormal(v.normal));
                float3 offset = sin(v.vertex.xyz * _has + _speed * _Time.y);

                WorldVertex = WorldVertex + normal * _amp * offset;

                o.vertex = TransformWorldToHClip(WorldVertex);

                return o;
            }

            half4 frag (v2f i) : SV_Target
            {
               
                return half4(1,0,0,1);
            }
            ENDHLSL
        }
    }
}
